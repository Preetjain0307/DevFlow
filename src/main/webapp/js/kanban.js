/**
 * DevFlow Interactive Kanban Board Engine
 * Features: HTML5 Drag & Drop, Smooth Ghost Preview, Empty State Toggles,
 * Dynamic Badge Counters, Real-time AJAX persistence, and Floating Toasts.
 */

document.addEventListener("DOMContentLoaded", function () {
    const cards = document.querySelectorAll(".kanban-card");
    const dropZones = document.querySelectorAll(".kanban-cards-container, .kanban-droppable");

    let draggedCard = null;
    let sourceContainer = null;

    function getStatusLabel(status) {
        switch (status) {
            case "TODO": return "To Do";
            case "IN_PROGRESS": return "In Progress";
            case "IN_REVIEW": return "In Review";
            case "COMPLETED": return "Done";
            default: return status;
        }
    }

    function updateCardAppearance(card, status) {
        const titleLink = card.querySelector(".card-title a");
        if (status === "COMPLETED") {
            card.classList.add("opacity-75");
            if (titleLink) {
                titleLink.classList.add("text-muted", "text-decoration-line-through");
            }
        } else {
            card.classList.remove("opacity-75");
            if (titleLink) {
                titleLink.classList.remove("text-muted", "text-decoration-line-through");
            }
        }
    }

    function updateColumnCountersAndHints() {
        document.querySelectorAll(".kanban-column").forEach(col => {
            const container = col.querySelector(".kanban-cards-container, .kanban-droppable");
            const countBadge = col.querySelector(".kanban-column-count");
            const emptyHint = col.querySelector(".kanban-empty-hint");

            if (!container) return;
            const currentCards = container.querySelectorAll(".kanban-card");
            const count = currentCards.length;

            if (countBadge) countBadge.textContent = count;
            if (emptyHint) {
                if (count === 0) {
                    emptyHint.classList.remove("d-none");
                } else {
                    emptyHint.classList.add("d-none");
                }
            }
        });
    }

    function initCardDrag(card) {
        card.setAttribute("draggable", "true");

        card.addEventListener("dragstart", function (e) {
            draggedCard = this;
            sourceContainer = this.parentElement;
            this.classList.add("is-dragging", "opacity-50");
            e.dataTransfer.effectAllowed = "move";
            e.dataTransfer.setData("text/plain", this.getAttribute("data-task-id"));
        });

        card.addEventListener("dragend", function () {
            this.classList.remove("is-dragging", "opacity-50");
            dropZones.forEach(zone => zone.classList.remove("drag-over"));
            draggedCard = null;
            sourceContainer = null;
        });
    }

    cards.forEach(initCardDrag);

    dropZones.forEach(container => {
        container.addEventListener("dragover", function (e) {
            e.preventDefault();
            e.dataTransfer.dropEffect = "move";
            this.classList.add("drag-over");
        });

        container.addEventListener("dragleave", function (e) {
            // Only remove if leaving container boundary
            if (!this.contains(e.relatedTarget)) {
                this.classList.remove("drag-over");
            }
        });

        container.addEventListener("drop", function (e) {
            e.preventDefault();
            this.classList.remove("drag-over");

            if (!draggedCard) return;

            const targetContainer = this;
            const originalContainer = sourceContainer;
            const taskId = draggedCard.getAttribute("data-task-id");
            const taskKey = draggedCard.getAttribute("data-task-key") || ("Task #" + taskId);
            const targetStatus = targetContainer.getAttribute("data-status");
            const originalStatus = originalContainer ? originalContainer.getAttribute("data-status") : null;

            if (targetStatus === originalStatus) return; // No change

            // Move element in DOM
            targetContainer.appendChild(draggedCard);
            updateCardAppearance(draggedCard, targetStatus);
            updateColumnCountersAndHints();

            // Persist status update to backend
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1) || window.location.pathname.length);
            const params = new URLSearchParams();
            params.append("taskId", taskId);
            params.append("status", targetStatus);

            fetch(contextPath + "/task/status-update", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                body: params.toString()
            })
            .then(res => {
                if (!res.ok) throw new Error("HTTP error " + res.status);
                return res.json();
            })
            .then(data => {
                if (data.success) {
                    if (window.DevFlow && window.DevFlow.toast) {
                        window.DevFlow.toast(
                            `<strong>${taskKey}</strong> was moved to <strong>${getStatusLabel(targetStatus)}</strong>.`,
                            "success",
                            "Status Updated"
                        );
                    }
                } else {
                    throw new Error(data.error || "Server rejected status update");
                }
            })
            .catch(err => {
                console.error("Failed to update task status:", err);
                // Revert card to original column on error
                if (originalContainer) {
                    originalContainer.appendChild(draggedCard);
                    updateCardAppearance(draggedCard, originalStatus);
                    updateColumnCountersAndHints();
                }
                if (window.DevFlow && window.DevFlow.toast) {
                    window.DevFlow.toast(`Could not update ${taskKey}: ${err.message}`, "danger", "Update Failed");
                }
            });
        });
    });

    // Initial pass to ensure counters and empty hints are accurate
    updateColumnCountersAndHints();
});
