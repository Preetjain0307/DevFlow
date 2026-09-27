// Global DevFlow Namespace & Modern Floating Toast Notification System
window.DevFlow = window.DevFlow || {};
window.DevFlow.toast = function(message, type = 'info', title = null) {
    let container = document.getElementById('devflow-toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'devflow-toast-container';
        container.className = 'toast-container position-fixed top-0 end-0 p-3';
        container.style.zIndex = '1090';
        document.body.appendChild(container);
    }

    const icons = {
        success: 'bi-check-circle-fill text-success',
        warning: 'bi-exclamation-triangle-fill text-warning',
        danger: 'bi-x-circle-fill text-danger',
        info: 'bi-info-circle-fill text-primary'
    };
    const icon = icons[type] || icons.info;
    const toastTitle = title || (type === 'success' ? 'Success' : (type === 'danger' ? 'Error' : 'Notice'));

    const toastEl = document.createElement('div');
    toastEl.className = 'toast df-toast shadow-lg border-0 mb-2';
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    toastEl.innerHTML = `
        <div class="toast-header border-bottom-0 pb-1">
            <i class="bi ${icon} me-2 fs-6"></i>
            <strong class="me-auto df-toast-title">${toastTitle}</strong>
            <small class="text-muted">Just now</small>
            <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body pt-1 df-toast-body">
            ${message}
        </div>
    `;

    container.appendChild(toastEl);
    if (window.bootstrap && window.bootstrap.Toast) {
        const bsToast = new bootstrap.Toast(toastEl, { delay: 3500 });
        bsToast.show();
        toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
    } else {
        setTimeout(() => toastEl.remove(), 3500);
    }
};

document.addEventListener("DOMContentLoaded", function () {
    // 1. Initialize Bootstrap Tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // 2. Sidebar Toggle for Mobile View
    const sidebarToggleBtn = document.getElementById("sidebarToggleBtn");
    const sidebar = document.querySelector(".sidebar");
    if (sidebarToggleBtn && sidebar) {
        sidebarToggleBtn.addEventListener("click", function () {
            sidebar.classList.toggle("open");
        });
    }

    // 3. Auto dismiss flash alerts after 5 seconds
    const flashAlerts = document.querySelectorAll(".alert-dismissible");
    flashAlerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // 4. Notification Count Poller
    function pollNotificationCount() {
        fetch(window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1) || window.location.pathname.length) + '/api/notifications/count')
            .then(res => {
                if (res.ok) return res.json();
                throw new Error("Failed to fetch notification count");
            })
            .then(data => {
                const badge = document.getElementById("navNotificationBadge");
                if (badge && data.unreadCount !== undefined) {
                    if (data.unreadCount > 0) {
                        badge.textContent = data.unreadCount > 99 ? '99+' : data.unreadCount;
                        badge.classList.remove("d-none");
                    } else {
                        badge.classList.add("d-none");
                    }
                }
            })
            .catch(() => {
                // Silently handle if unauthenticated or on login page
            });
    }

    // Initial poll & 30s interval
    pollNotificationCount();
    setInterval(pollNotificationCount, 30000);

    // 5. System-wide Dark / Light Mode Toggle Engine
    function initTheme() {
        const toggleBtn = document.getElementById("themeToggleBtn");
        const toggleIcon = document.getElementById("themeToggleIcon");
        if (!toggleBtn || !toggleIcon) return;

        function updateIcon(theme) {
            if (theme === "dark") {
                toggleIcon.className = "bi bi-sun-fill fs-5 text-warning";
                toggleBtn.setAttribute("title", "Switch to Light Mode");
                toggleBtn.classList.remove("text-secondary");
            } else {
                toggleIcon.className = "bi bi-moon-stars-fill fs-5";
                toggleBtn.setAttribute("title", "Switch to Dark Mode");
                toggleBtn.classList.add("text-secondary");
            }
        }

        const currentTheme = document.documentElement.getAttribute("data-bs-theme") || "light";
        updateIcon(currentTheme);

        toggleBtn.addEventListener("click", function () {
            const current = document.documentElement.getAttribute("data-bs-theme") || "light";
            const nextTheme = current === "dark" ? "light" : "dark";

            document.documentElement.setAttribute("data-bs-theme", nextTheme);
            document.documentElement.setAttribute("data-theme", nextTheme);
            try {
                localStorage.setItem("devflow-theme", nextTheme);
            } catch (e) {}

            updateIcon(nextTheme);

            // Dispatch custom event for Chart.js and dynamic components
            window.dispatchEvent(new CustomEvent("devflow:themeChanged", { detail: { theme: nextTheme } }));
        });
    }

    initTheme();

    // 6. Global Quick Search (Ctrl + K) Spotlight Engine
    function initQuickSearch() {
        const modalEl = document.getElementById("quickSearchModal");
        if (!modalEl) return;

        const modal = new bootstrap.Modal(modalEl);
        const searchInput = document.getElementById("globalSearchInput");
        const defaultHint = document.getElementById("searchDefaultHint");
        const spinner = document.getElementById("searchResultsSpinner");
        const resultsList = document.getElementById("searchResultsList");

        // Global Keybinding: Ctrl + K or Cmd + K
        window.addEventListener("keydown", function (e) {
            if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "k") {
                e.preventDefault();
                modal.show();
            }
        });

        modalEl.addEventListener("shown.bs.modal", function () {
            if (searchInput) {
                searchInput.focus();
                searchInput.select();
            }
        });

        modalEl.addEventListener("hidden.bs.modal", function () {
            if (searchInput) searchInput.value = "";
            if (resultsList) {
                resultsList.innerHTML = "";
                resultsList.classList.add("d-none");
            }
            if (defaultHint) defaultHint.classList.remove("d-none");
            if (spinner) spinner.classList.add("d-none");
        });

        let debounceTimer = null;
        let selectedIndex = -1;

        if (searchInput) {
            searchInput.addEventListener("input", function () {
                const query = this.value.trim();
                clearTimeout(debounceTimer);

                if (query.length < 2) {
                    if (resultsList) {
                        resultsList.innerHTML = "";
                        resultsList.classList.add("d-none");
                    }
                    if (spinner) spinner.classList.add("d-none");
                    if (defaultHint) defaultHint.classList.remove("d-none");
                    return;
                }

                if (defaultHint) defaultHint.classList.add("d-none");
                if (spinner) spinner.classList.remove("d-none");

                debounceTimer = setTimeout(() => {
                    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1) || window.location.pathname.length);
                    fetch(contextPath + "/api/search?q=" + encodeURIComponent(query))
                        .then(res => {
                            if (!res.ok) throw new Error("Search failed: " + res.status);
                            return res.json();
                        })
                        .then(data => {
                            if (spinner) spinner.classList.add("d-none");
                            if (!resultsList) return;
                            resultsList.innerHTML = "";
                            selectedIndex = -1;

                            if (!data.results || data.results.length === 0) {
                                resultsList.classList.remove("d-none");
                                resultsList.innerHTML = `
                                    <div class="text-center py-4 text-muted">
                                        <i class="bi bi-search fs-2 opacity-50 d-block mb-1"></i>
                                        <span>No results found for "<strong>` + escapeHtml(query) + `</strong>"</span>
                                    </div>
                                `;
                                return;
                            }

                            resultsList.classList.remove("d-none");
                            data.results.forEach((item, idx) => {
                                const a = document.createElement("a");
                                a.href = item.url;
                                a.className = "list-group-item list-group-item-action d-flex align-items-center justify-content-between p-3 border-0 border-bottom df-search-result-item";
                                a.setAttribute("data-index", idx);
                                a.innerHTML = `
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="rounded-3 p-2 bg-light border d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                                            <i class="bi ${item.icon} fs-5"></i>
                                        </div>
                                        <div>
                                            <div class="fw-semibold text-dark mb-0">${escapeHtml(item.title)}</div>
                                            <small class="text-muted">${escapeHtml(item.subtitle)}</small>
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25">${item.category}</span>
                                        <i class="bi bi-chevron-right text-muted small"></i>
                                    </div>
                                `;
                                resultsList.appendChild(a);
                            });
                        })
                        .catch(err => {
                            if (spinner) spinner.classList.add("d-none");
                            console.error("Search error:", err);
                        });
                }, 200);
            });

            // Keyboard arrow navigation in results
            searchInput.addEventListener("keydown", function (e) {
                if (!resultsList) return;
                const items = resultsList.querySelectorAll(".df-search-result-item");
                if (items.length === 0) return;

                if (e.key === "ArrowDown") {
                    e.preventDefault();
                    selectedIndex = (selectedIndex + 1) % items.length;
                    updateSelectedResult(items, selectedIndex);
                } else if (e.key === "ArrowUp") {
                    e.preventDefault();
                    selectedIndex = (selectedIndex - 1 + items.length) % items.length;
                    updateSelectedResult(items, selectedIndex);
                } else if (e.key === "Enter") {
                    if (selectedIndex >= 0 && items[selectedIndex]) {
                        e.preventDefault();
                        items[selectedIndex].click();
                    }
                }
            });
        }

        function updateSelectedResult(items, idx) {
            items.forEach((item, i) => {
                if (i === idx) {
                    item.classList.add("active");
                    item.scrollIntoView({ block: "nearest" });
                } else {
                    item.classList.remove("active");
                }
            });
        }

        function escapeHtml(str) {
            if (!str) return "";
            return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
        }
    }

    initQuickSearch();

    // 7. Academic Architecture & Viva Voce Modal Live Telemetry
    function initVivaVoceModal() {
        const modalEl = document.getElementById("vivaArchitectureModal");
        if (!modalEl) return;

        const refreshBtn = document.getElementById("btnRefreshTelemetry");

        function loadTelemetry() {
            const refreshIcon = refreshBtn ? refreshBtn.querySelector("i") : null;
            if (refreshIcon) refreshIcon.classList.add("spin-animation");

            fetch(contextPath + "/api/architecture/status")
                .then(res => {
                    if (!res.ok) throw new Error("Status " + res.status);
                    return res.json();
                })
                .then(data => {
                    if (refreshIcon) refreshIcon.classList.remove("spin-animation");

                    // 1. Connection Pool Stats
                    if (data.pool) {
                        setText("telPoolName", data.pool.poolName || "DevFlowHikariPool");
                        setText("telActiveConns", data.pool.activeConnections ?? 0);
                        setText("telIdleConns", data.pool.idleConnections ?? 0);
                        setText("telMaxPool", data.pool.maxPoolSize ?? 10);
                        setText("telMinIdle", data.pool.minimumIdle ?? 2);
                        setText("telConnTimeout", data.pool.connectionTimeout || "20000ms");
                        setText("telIdleTimeout", data.pool.idleTimeout || "30000ms");
                        setText("telThreadsAwaiting", data.pool.threadsAwaitingConnection ?? 0);
                    }

                    // 2. MySQL Metadata
                    if (data.database) {
                        setText("telDbProduct", data.database.productName || "MySQL");
                        setText("telDbVersion", data.database.productVersion || "5.5.5-MariaDB");
                        setText("telDbDriver", data.database.driverName || "MySQL Connector/J");
                        setText("telDbName", data.database.databaseName || "devflow_db");
                        setText("telDbTables", (data.database.totalTables || 26) + " Tables");
                    }

                    // 3. JVM Runtime
                    if (data.runtime) {
                        setText("telJavaVer", data.runtime.javaVersion || "17");
                        setText("telJavaVendor", data.runtime.javaVendor || "Oracle Corporation");
                        setText("telOsName", data.runtime.osName || "Windows");
                        setText("telCpuCores", data.runtime.availableProcessors + " Cores");
                        setText("telServerInfo", data.runtime.serverInfo || "Apache Tomcat");
                        setText("telUsedMem", data.runtime.usedMemoryMB ?? 0);
                        setText("telTotalMem", data.runtime.totalMemoryMB ?? 0);
                        setText("telMaxMem", data.runtime.maxMemoryMB ?? 0);

                        const memBar = document.getElementById("telMemBar");
                        if (memBar && data.runtime.totalMemoryMB > 0) {
                            const pct = Math.min(100, Math.round((data.runtime.usedMemoryMB / data.runtime.totalMemoryMB) * 100));
                            memBar.style.width = pct + "%";
                            memBar.setAttribute("aria-valuenow", pct);
                            memBar.textContent = pct + "%";
                        }
                    }
                })
                .catch(err => {
                    if (refreshIcon) refreshIcon.classList.remove("spin-animation");
                    console.error("Viva telemetry fetch error:", err);
                });
        }

        function setText(id, text) {
            const el = document.getElementById(id);
            if (el) el.textContent = text;
        }

        // Auto load on modal open
        modalEl.addEventListener("shown.bs.modal", loadTelemetry);

        if (refreshBtn) {
            refreshBtn.addEventListener("click", loadTelemetry);
        }
    }

    initVivaVoceModal();

    // 7. Smooth AJAX Activity, Comments, and Voting (Feature 3.2)
    function initAjaxInterceptors() {
        document.addEventListener('submit', function (e) {
            const form = e.target;
            if (form.matches('[data-ajax="true"], #taskCommentForm, #bugCommentForm, #ideaCommentForm, #taskStatusForm, #bugStatusForm, .ajax-vote-form')) {
                e.preventDefault();
                const formData = new FormData(form);
                const submitBtn = form.querySelector('button[type="submit"]');
                const originalBtnHtml = submitBtn ? submitBtn.innerHTML : '';
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status"></span>Updating...';
                }

                const url = form.action;
                const params = new URLSearchParams();
                for (const [key, value] of formData.entries()) {
                    params.append(key, value);
                }

                fetch(url, {
                    method: 'POST',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest',
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: params.toString()
                })
                .then(res => {
                    if (!res.ok) throw new Error('HTTP ' + res.status);
                    return res.json();
                })
                .then(data => {
                    if (submitBtn) {
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = originalBtnHtml;
                    }
                    if (!data.success) {
                        if (window.DevFlow && window.DevFlow.toast) {
                            window.DevFlow.toast(data.error || 'Failed to complete request.', 'danger');
                        }
                        return;
                    }

                    // Handle Comment submissions (Task, Bug, Idea)
                    if (form.id === 'taskCommentForm' || form.id === 'bugCommentForm' || form.id === 'ideaCommentForm') {
                        const textarea = form.querySelector('textarea');
                        if (textarea) textarea.value = '';

                        const listId = form.id === 'taskCommentForm' ? 'taskCommentsList' :
                                       (form.id === 'bugCommentForm' ? 'bugCommentsList' : 'ideaCommentsList');
                        const countId = form.id === 'taskCommentForm' ? 'taskCommentCount' :
                                        (form.id === 'bugCommentForm' ? 'bugCommentCount' : 'ideaCommentCount');
                        const commentsList = document.getElementById(listId);
                        const countBadge = document.getElementById(countId);

                        if (commentsList) {
                            // Remove empty placeholder if any
                            const emptyPlaceholder = commentsList.querySelector('.text-muted.text-center');
                            if (emptyPlaceholder) emptyPlaceholder.remove();

                            const avatarBg = form.id === 'bugCommentForm' ? 'bg-danger text-white' : (form.id === 'ideaCommentForm' ? 'bg-warning text-dark' : 'bg-primary text-white');
                            const initial = data.initial || (data.authorName ? data.authorName.charAt(0).toUpperCase() : 'U');

                            const newCommentEl = document.createElement('div');
                            newCommentEl.className = 'd-flex mb-3 fade-in-slide';
                            newCommentEl.innerHTML = `
                                <div class="avatar ${avatarBg} rounded-circle me-3 d-flex align-items-center justify-content-center flex-shrink-0" style="width:36px;height:36px;font-weight:600;">
                                    \${initial}
                                </div>
                                <div class="flex-grow-1 bg-light p-3 rounded border border-light-subtle shadow-sm">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="fw-bold text-dark">\${escapeHtml(data.authorName)}</span>
                                        <small class="text-muted"><i class="bi bi-clock me-1"></i>Just now</small>
                                    </div>
                                    <p class="mb-0 small" style="white-space: pre-wrap;">\${escapeHtml(data.comment)}</p>
                                </div>
                            `;
                            commentsList.prepend(newCommentEl);
                        }

                        if (countBadge) {
                            const currentMatch = countBadge.textContent.match(/\\d+/);
                            const currentCount = currentMatch ? parseInt(currentMatch[0]) : 0;
                            countBadge.textContent = (currentCount + 1) + ' Comments';
                        }

                        if (window.DevFlow && window.DevFlow.toast) {
                            window.DevFlow.toast('Comment posted successfully!', 'success');
                        }
                    }

                    // Handle Status Updates (Task, Bug)
                    else if (form.id === 'taskStatusForm' || form.id === 'bugStatusForm') {
                        const statusBadge = document.getElementById('headerStatusBadge') || document.querySelector('.page-header .badge');
                        if (statusBadge && data.status) {
                            statusBadge.textContent = data.status;
                        }
                        if (window.DevFlow && window.DevFlow.toast) {
                            window.DevFlow.toast('Status successfully updated to ' + data.status, 'success');
                        }
                    }

                    // Handle Idea Votes
                    else if (form.classList.contains('ajax-vote-form') || form.id === 'ideaVoteForm') {
                        const upvotesEl = document.getElementById('ideaUpvotesCount');
                        const downvotesEl = document.getElementById('ideaDownvotesCount');
                        if (upvotesEl && data.upvotes !== undefined) {
                            upvotesEl.textContent = data.upvotes;
                            upvotesEl.classList.add('scale-up');
                            setTimeout(() => upvotesEl.classList.remove('scale-up'), 300);
                        }
                        if (downvotesEl && data.downvotes !== undefined) {
                            downvotesEl.textContent = data.downvotes;
                            downvotesEl.classList.add('scale-up');
                            setTimeout(() => downvotesEl.classList.remove('scale-up'), 300);
                        }

                        const btnUp = document.getElementById('btnVoteUp');
                        const btnDown = document.getElementById('btnVoteDown');
                        const badgeEl = document.getElementById('userVoteBadge');
                        const isUp = (data.vote === 'YES' || data.vote === 'UPVOTE');

                        if (btnUp && btnDown) {
                            if (isUp) {
                                btnUp.className = 'btn btn-success text-white px-3 shadow-sm';
                                btnDown.className = 'btn btn-outline-danger px-3 shadow-sm';
                            } else {
                                btnDown.className = 'btn btn-danger text-white px-3 shadow-sm';
                                btnUp.className = 'btn btn-outline-success px-3 shadow-sm';
                            }
                        }

                        if (badgeEl) {
                            if (isUp) {
                                badgeEl.innerHTML = '<span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle me-1"></i>You upvoted this proposal</span>';
                            } else {
                                badgeEl.innerHTML = '<span class="badge bg-danger-subtle text-danger border border-danger-subtle"><i class="bi bi-x-circle me-1"></i>You downvoted this proposal</span>';
                            }
                        }

                        if (window.DevFlow && window.DevFlow.toast) {
                            window.DevFlow.toast(isUp ? 'Upvote recorded successfully!' : 'Downvote recorded successfully!', isUp ? 'success' : 'info');
                        }
                    }
                })
                .catch(err => {
                    if (submitBtn) {
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = originalBtnHtml;
                    }
                    console.error('AJAX form error, falling back to full submission:', err);
                    form.submit();
                });
            }
        });

        function escapeHtml(str) {
            if (!str) return '';
            return str.replace(/&/g, '&amp;')
                      .replace(/</g, '&lt;')
                      .replace(/>/g, '&gt;')
                      .replace(/"/g, '&quot;')
                      .replace(/'/g, '&#039;');
        }
    }

    initAjaxInterceptors();
});
