/**
 * DevFlow Chart.js Analytics Initializer - Theme Aware & Polished
 * Features:
 * - Dynamic theme switching (Light / Dark mode)
 * - Custom Doughnut Center Total metric readout plugin
 * - Vertical gradient bar fills with rounded corners
 * - Modern arc spacing & rounded borders for doughnuts
 * - Sleek floating tooltips with point styles and glassmorphism
 * - Area gradient fills for line charts (Sprint Burndown / Velocity)
 * - Backward compatibility aliases for /reports view (initTaskChart, initBugChart)
 */

const devflowCharts = [];

function isDarkMode() {
    return document.documentElement.getAttribute('data-bs-theme') === 'dark' ||
           document.documentElement.getAttribute('data-theme') === 'dark';
}

function getThemeColors() {
    const dark = isDarkMode();
    return {
        dark: dark,
        textColor: dark ? '#cbd5e1' : '#475569',
        textMuted: dark ? '#94a3b8' : '#64748b',
        titleColor: dark ? '#f8fafc' : '#0f172a',
        gridColor: dark ? 'rgba(255, 255, 255, 0.06)' : 'rgba(0, 0, 0, 0.05)',
        cardBg: dark ? '#1e293b' : '#ffffff',
        tooltipBg: dark ? 'rgba(15, 23, 42, 0.95)' : 'rgba(255, 255, 255, 0.98)',
        tooltipBorder: dark ? 'rgba(255, 255, 255, 0.12)' : 'rgba(0, 0, 0, 0.08)'
    };
}

function getCommonTooltipConfig(colors) {
    return {
        enabled: true,
        backgroundColor: colors.tooltipBg,
        titleColor: colors.titleColor,
        bodyColor: colors.textColor,
        borderColor: colors.tooltipBorder,
        borderWidth: 1,
        padding: 10,
        boxPadding: 6,
        usePointStyle: true,
        cornerRadius: 8,
        titleFont: { size: 13, weight: '600' },
        bodyFont: { size: 12 },
        animation: { duration: 150 }
    };
}

// Custom plugin to render total count in the center of doughnut charts
const doughnutCenterTextPlugin = {
    id: 'doughnutCenterText',
    beforeDraw(chart) {
        if (chart.config.type !== 'doughnut') return;
        const { ctx, chartArea } = chart;
        if (!chartArea) return;

        const dataset = chart.data.datasets[0];
        if (!dataset || !dataset.data) return;

        const total = dataset.data.reduce((sum, val) => sum + Number(val || 0), 0);
        const colors = getThemeColors();

        ctx.save();
        const centerX = (chartArea.left + chartArea.right) / 2;
        const centerY = (chartArea.top + chartArea.bottom) / 2;

        // Metric Number
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.font = '700 22px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif';
        ctx.fillStyle = colors.titleColor;
        ctx.fillText(total.toString(), centerX, centerY - 6);

        // Metric Label
        ctx.font = '600 10px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif';
        ctx.fillStyle = colors.textMuted;
        ctx.fillText('TOTAL', centerX, centerY + 14);

        ctx.restore();
    }
};

if (typeof Chart !== 'undefined') {
    Chart.register(doughnutCenterTextPlugin);
}

// 1. Task Status Doughnut Chart
function initTaskStatusChart(canvasId, dataMap) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const colors = getThemeColors();
    const ctx = canvas.getContext('2d');

    const chart = new Chart(ctx, {
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
                hoverBackgroundColor: ['#64748b', '#4338ca', '#d97706', '#059669'],
                borderWidth: 3,
                borderColor: colors.cardBg,
                borderRadius: 4,
                spacing: 3,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '72%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 10,
                        boxHeight: 10,
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 14,
                        color: colors.textColor,
                        font: { size: 12, weight: '500' }
                    }
                },
                tooltip: getCommonTooltipConfig(colors)
            },
            animation: {
                animateScale: true,
                animateRotate: true,
                duration: 900
            }
        }
    });

    devflowCharts.push({ chart, type: 'doughnut' });
}

// 2. Bug Severity Bar Chart with Gradient Fills & Rounded Corners
function initBugSeverityChart(canvasId, dataMap) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const colors = getThemeColors();
    const ctx = canvas.getContext('2d');

    // Create subtle vertical gradients for each severity level
    function createGrad(c1, c2) {
        const grad = ctx.createLinearGradient(0, 0, 0, 220);
        grad.addColorStop(0, c1);
        grad.addColorStop(1, c2);
        return grad;
    }

    const gradLow = createGrad('#06b6d4', 'rgba(6, 182, 212, 0.45)');
    const gradMed = createGrad('#f59e0b', 'rgba(245, 158, 11, 0.45)');
    const gradHigh = createGrad('#ea580c', 'rgba(234, 88, 12, 0.45)');
    const gradCrit = createGrad('#ef4444', 'rgba(239, 68, 68, 0.45)');

    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Low', 'Medium', 'High', 'Critical'],
            datasets: [{
                label: 'Defects',
                data: [
                    dataMap.LOW || 0,
                    dataMap.MEDIUM || 0,
                    dataMap.HIGH || 0,
                    dataMap.CRITICAL || 0
                ],
                backgroundColor: [gradLow, gradMed, gradHigh, gradCrit],
                borderColor: ['#06b6d4', '#f59e0b', '#ea580c', '#ef4444'],
                borderWidth: 1.5,
                borderRadius: 8,
                borderSkipped: false,
                barPercentage: 0.65
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1,
                        color: colors.textColor,
                        font: { size: 11 }
                    },
                    grid: {
                        color: colors.gridColor,
                        drawBorder: false
                    }
                },
                x: {
                    ticks: {
                        color: colors.textColor,
                        font: { size: 12, weight: '500' }
                    },
                    grid: { display: false }
                }
            },
            plugins: {
                legend: { display: false },
                tooltip: getCommonTooltipConfig(colors)
            },
            animation: {
                duration: 800
            }
        }
    });

    devflowCharts.push({ chart, type: 'bar' });
}

// 3. Idea Proposals Status Pie Chart
function initIdeaStatusChart(canvasId, dataMap) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const colors = getThemeColors();
    const ctx = canvas.getContext('2d');

    const chart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['In Voting', 'Pending Faculty', 'Approved', 'Rejected', 'Changes Req.'],
            datasets: [{
                data: [
                    dataMap.IN_VOTING || 0,
                    dataMap.PENDING_FACULTY || 0,
                    dataMap.APPROVED || 0,
                    dataMap.REJECTED || 0,
                    dataMap.CHANGES_REQUESTED || 0
                ],
                backgroundColor: ['#3b82f6', '#8b5cf6', '#10b981', '#ef4444', '#f59e0b'],
                hoverBackgroundColor: ['#2563eb', '#7c3aed', '#059669', '#dc2626', '#d97706'],
                borderWidth: 2,
                borderColor: colors.cardBg,
                borderRadius: 4,
                spacing: 2,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 10,
                        boxHeight: 10,
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 12,
                        color: colors.textColor,
                        font: { size: 11, weight: '500' }
                    }
                },
                tooltip: getCommonTooltipConfig(colors)
            },
            animation: {
                animateRotate: true,
                duration: 900
            }
        }
    });

    devflowCharts.push({ chart, type: 'pie' });
}

// 4. Area Line Chart with Gradient Fill (Sprint Burndown / Velocity)
function initSprintBurndownChart(canvasId, labels, idealPoints, actualPoints) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const colors = getThemeColors();
    const ctx = canvas.getContext('2d');

    const actualGrad = ctx.createLinearGradient(0, 0, 0, 240);
    actualGrad.addColorStop(0, 'rgba(79, 70, 229, 0.35)');
    actualGrad.addColorStop(1, 'rgba(79, 70, 229, 0.0)');

    const chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Ideal Burndown',
                    data: idealPoints,
                    borderColor: '#94a3b8',
                    borderWidth: 2,
                    borderDash: [5, 5],
                    fill: false,
                    pointRadius: 0
                },
                {
                    label: 'Actual Remaining',
                    data: actualPoints,
                    borderColor: '#4f46e5',
                    backgroundColor: actualGrad,
                    borderWidth: 2.5,
                    fill: true,
                    tension: 0.35,
                    pointBackgroundColor: '#4f46e5',
                    pointBorderColor: colors.cardBg,
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { color: colors.textColor },
                    grid: { color: colors.gridColor }
                },
                x: {
                    ticks: { color: colors.textColor },
                    grid: { display: false }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: { color: colors.textColor, usePointStyle: true, boxWidth: 8 }
                },
                tooltip: getCommonTooltipConfig(colors)
            }
        }
    });

    devflowCharts.push({ chart, type: 'line' });
}

// Backward Compatibility Aliases for /reports and older views
function initTaskChart(canvasId, todo, inProgress, inReview, completed) {
    initTaskStatusChart(canvasId, {
        TODO: todo,
        IN_PROGRESS: inProgress,
        IN_REVIEW: inReview,
        COMPLETED: completed
    });
}

function initBugChart(canvasId, low, medium, high, critical) {
    initBugSeverityChart(canvasId, {
        LOW: low,
        MEDIUM: medium,
        HIGH: high,
        CRITICAL: critical
    });
}

// 5. Dynamic Theme Change Listener: Re-sync colors for all live charts
window.addEventListener('devflow:themeChanged', function() {
    const colors = getThemeColors();
    devflowCharts.forEach(({ chart, type }) => {
        if (!chart) return;

        // Update Legend
        if (chart.options.plugins && chart.options.plugins.legend) {
            chart.options.plugins.legend.labels.color = colors.textColor;
        }

        // Update Tooltips
        if (chart.options.plugins && chart.options.plugins.tooltip) {
            const tt = chart.options.plugins.tooltip;
            tt.backgroundColor = colors.tooltipBg;
            tt.titleColor = colors.titleColor;
            tt.bodyColor = colors.textColor;
            tt.borderColor = colors.tooltipBorder;
        }

        // Update Scales (Bar / Line)
        if ((type === 'bar' || type === 'line') && chart.options.scales) {
            if (chart.options.scales.x && chart.options.scales.x.ticks) {
                chart.options.scales.x.ticks.color = colors.textColor;
            }
            if (chart.options.scales.y) {
                if (chart.options.scales.y.ticks) chart.options.scales.y.ticks.color = colors.textColor;
                if (chart.options.scales.y.grid) chart.options.scales.y.grid.color = colors.gridColor;
            }
        }

        // Update Borders (Doughnut / Pie)
        if (type === 'doughnut' || type === 'pie') {
            chart.data.datasets.forEach(ds => {
                ds.borderColor = colors.cardBg;
            });
        }

        chart.update();
    });
});
