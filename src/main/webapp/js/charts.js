/**
 * DevFlow Chart.js Analytics Initializer
 */

function initTaskStatusChart(canvasId, dataMap) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return;

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['To Do', 'In Progress', 'In Review', 'Completed'],
            datasets: [{
                data: [
                    dataMap.TODO || 0,
                    dataMap.IN_PROGRESS || 0,
                    dataMap.IN_REVIEW || 0,
                    dataMap.COMPLETED || 0
                ],
                backgroundColor: ['#94a3b8', '#4f46e5', '#f59e0b', '#10b981'],
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { boxWidth: 12, padding: 15 }
                }
            },
            cutout: '70%'
        }
    });
}

function initBugSeverityChart(canvasId, dataMap) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Low', 'Medium', 'High', 'Critical'],
            datasets: [{
                label: 'Bugs',
                data: [
                    dataMap.LOW || 0,
                    dataMap.MEDIUM || 0,
                    dataMap.HIGH || 0,
                    dataMap.CRITICAL || 0
                ],
                backgroundColor: ['#06b6d4', '#f59e0b', '#ea580c', '#ef4444'],
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { stepSize: 1 }
                }
            },
            plugins: {
                legend: { display: false }
            }
        }
    });
}

function initIdeaStatusChart(canvasId, dataMap) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return;

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['In Voting', 'Pending Faculty', 'Approved', 'Rejected', 'Changes Requested'],
            datasets: [{
                data: [
                    dataMap.IN_VOTING || 0,
                    dataMap.PENDING_FACULTY || 0,
                    dataMap.APPROVED || 0,
                    dataMap.REJECTED || 0,
                    dataMap.CHANGES_REQUESTED || 0
                ],
                backgroundColor: ['#3b82f6', '#8b5cf6', '#10b981', '#ef4444', '#f59e0b']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { boxWidth: 12, padding: 12 }
                }
            }
        }
    });
}
