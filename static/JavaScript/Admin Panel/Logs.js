const API = "http://localhost:8082/api/attendance/logs";

let allLogs = [];

// ✅ Page load pe database se data lo
window.onload = function() {
    fetch(API)
    .then(res => res.json())
    .then(data => {
        allLogs = data;
        renderLogs(allLogs);
    })
    .catch(err => {
        logTable.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center;color:red;">
                    Error loading logs! Server chal raha hai?
                </td>
            </tr>`;
    });
};

// ✅ Same render function — sirf database data use ho raha hai
function renderLogs(list) {
    logTable.innerHTML = "";

    if (list.length === 0) {
        logTable.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center;color:#888;">
                    No logs found
                </td>
            </tr>`;
        return;
    }

    list.forEach((l, i) => {
        logTable.innerHTML += `
            <tr>
                <td>${l.id}</td>
                <td>${l.user}</td>
                <td><span class="tag ${l.activity}">${l.activity.toUpperCase()}</span></td>
                <td>${l.description}</td>
                <td>${l.date}</td>
                <td>${l.time}</td>
            </tr>
        `;
    });
}

// ✅ Filter — same logic
function filterLogs() {
    const val = filter.value;
    if (val === "all") renderLogs(allLogs);
    else renderLogs(allLogs.filter(l => l.activity === val));
}

// ✅ Search — same logic
search.onkeyup = () => {
    const text = search.value.toLowerCase();
    const val = filter.value;

    let filtered = allLogs.filter(l =>
        l.description.toLowerCase().includes(text) ||
        l.user.toLowerCase().includes(text) ||
        l.date.includes(text)
    );

    if (val !== "all") {
        filtered = filtered.filter(l => l.activity === val);
    }

    renderLogs(filtered);
};