const API = "http://localhost:8082";

function addComment(noticeId) {
    let input   = document.getElementById("comment-" + noticeId);
    let comment = input.value.trim();

    if (!comment) {
        alert("Comment khali nahi ho sakta!");
        return;
    }

    let formData = new FormData();
    formData.append("noticeId", noticeId);
    formData.append("comment",  comment);

    fetch(API + "/employee/notices/comment", { method: "POST", body: formData })
    .then(res => res.text())
    .then(msg => {
        if (msg === "COMMENTED") {
            input.value = "";
            location.reload();
        }
    });
}