/**
 * DevFlow Core Frontend Application Logic
 */

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
});
