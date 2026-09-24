/**
 * DevFlow Kanban Board Drag-and-Drop & Status Logic
 */

document.addEventListener("DOMContentLoaded", function () {
    const cards = document.querySelectorAll(".kanban-card");
    const containers = document.querySelectorAll(".kanban-cards-container");

    let draggedCard = null;

    cards.forEach(card => {
        card.setAttribute("draggable", "true");

        card.addEventListener("dragstart", function (e) {
            draggedCard = this;
            this.classList.add("opacity-50");
            e.dataTransfer.effectAllowed = "move";
            e.dataTransfer.setData("text/plain", this.getAttribute("data-task-id"));
        });

        card.addEventListener("dragend", function () {
            this.classList.remove("opacity-50");
            draggedCard = null;
        });
    });

    containers.forEach(container => {
        container.addEventListener("dragover", function (e) {
            e.preventDefault();
            e.dataTransfer.dropEffect = "move";
            this.classList.add("bg-light");
        });

        container.addEventListener("dragleave", function () {
            this.classList.remove("bg-light");
        });

        container.addEventListener("drop", function (e) {
            e.preventDefault();
            this.classList.remove("bg-light");

            if (draggedCard) {
                const taskId = draggedCard.getAttribute("data-task-id");
                const newStatus = this.getAttribute("data-status");

                // Move element in DOM
                this.appendChild(draggedCard);

                // Update column badge counters
                updateColumnCounters();

                // Send AJAX update to server
                updateTaskStatusAjax(taskId, newStatus);
            }
        });
    });

    function updateColumnCounters() {
        document.querySelectorAll(".kanban-column").forEach(col => {
            const countBadge = col.querySelector(".kanban-column-count");
            const count = col.querySelectorAll(".kanban-card").length;
            if (countBadge) countBadge.textContent = count;
        });
    }

    function updateTaskStatusAjax(taskId, newStatus) {
        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1) || window.location.pathname.length);
        const params = new URLSearchParams();
        params.append("taskId", taskId);
        params.append("status", newStatus);

        fetch(contextPath + "/task/status-update", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: params.toString()
        })
        .then(res => res.json())
        .then(data => {
            if (!data.success) {
                console.error("Failed to update status on server:", data.error);
            }
        })
        .catch(err => {
            console.error("Network error updating task status:", err);
        });
    }

    // Quick move button handlers
    document.querySelectorAll(".btn-quick-move").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            const taskId = this.getAttribute("data-task-id");
            const newStatus = this.getAttribute("data-target-status");
            const targetContainer = document.querySelector(`.kanban-cards-container[data-status="${newStatus}"]`);
            const card = document.querySelector(`.kanban-card[data-task-id="${taskId}"]`);

            if (targetContainer && card) {
                targetContainer.appendChild(card);
                updateColumnCounters();
                updateTaskStatusAjax(taskId, newStatus);
            }
        });
    });
});
