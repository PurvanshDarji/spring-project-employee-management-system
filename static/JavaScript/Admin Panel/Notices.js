const API = "http://localhost:8082";

function openModal() {
    document.getElementById("modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
    document.getElementById("title").value = "";
    document.getElementById("message").value = "";
}

function submitNotice() {
    let title    = document.getElementById("title").value.trim();
    let message  = document.getElementById("message").value.trim();
    let category = document.getElementById("category").value;

    if (!title || !message) {
        alert("Title aur Message dono zaroori hain!");
        return;
    }

    let formData = new FormData();
    formData.append("title",    title);
    formData.append("message",  message);
    formData.append("category", category);

    fetch(API + "/admin/notices/post", { method: "POST", body: formData })
    .then(res => res.text())
    .then(msg => {
        if (msg === "POSTED") {
            alert("Notice posted successfully!");
            closeModal();
            location.reload();
        }
    });
}

function deleteNotice(id) {
    if (!confirm("Are you sure you want to delete this notice?")) return;

    fetch(API + "/admin/notices/delete/" + id, { method: "DELETE" })
    .then(res => res.text())
    .then(msg => {
        if (msg === "DELETED") {
            alert("Notice deleted!");
            location.reload();
        }
    });
}