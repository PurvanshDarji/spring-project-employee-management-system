// ✅ 1. Department Wise — Bar Chart
new Chart(document.getElementById("deptChart"), {
    type: "bar",
    data: {
        labels: deptLabels,
        datasets: [{
            label: "Employees",
            data: deptCounts,
            backgroundColor: [
                "#4f46e5","#06b6d4","#10b981",
                "#f59e0b","#ef4444","#8b5cf6"
            ],
            borderRadius: 8
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});

// ✅ 2. Overall Attendance — Donut Chart
new Chart(document.getElementById("attendancePie"), {
    type: "doughnut",
    data: {
        labels: ["Present", "Absent", "Half Day"],
        datasets: [{
            data: [presentCount, absentCount, halfDayCount],
            backgroundColor: ["#22c55e", "#ef4444", "#f59e0b"],
            borderWidth: 2
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { position: "bottom" }
        }
    }
});

// ✅ 3. Today's Attendance — Pie Chart
new Chart(document.getElementById("todayChart"), {
    type: "pie",
    data: {
        labels: ["Present Today", "Absent Today"],
        datasets: [{
            data: [todayPresent, todayAbsent],
            backgroundColor: ["#4f46e5", "#e5e7eb"],
            borderWidth: 2
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { position: "bottom" }
        }
    }
});

// ✅ 4. Attendance Status Split — Horizontal Bar
new Chart(document.getElementById("statusChart"), {
    type: "bar",
    data: {
        labels: ["Present", "Half Day", "Absent"],
        datasets: [{
            label: "Total Days",
            data: [presentCount, halfDayCount, absentCount],
            backgroundColor: ["#22c55e", "#f59e0b", "#ef4444"],
            borderRadius: 8
        }]
    },
    options: {
        indexAxis: "y", // ✅ horizontal bar
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});