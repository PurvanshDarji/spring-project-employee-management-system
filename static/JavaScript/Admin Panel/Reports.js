let feedbacks = JSON.parse(localStorage.getItem("feedbacks")) || [
    {name:"Rahul Sharma",dept:"IT",type:"Complaint",msg:"System slow during peak hours",date:"20 Jan 2026"},
    {name:"Anjali Patel",dept:"HR",type:"Suggestion",msg:"Monthly training workshops",date:"21 Jan 2026"},
    {name:"Amit Verma",dept:"Finance",type:"Complaint",msg:"Payroll delays",date:"22 Jan 2026"}
];

function renderTable(list=feedbacks){
    fbTable.innerHTML="";
    list.forEach((f,i)=>{
        fbTable.innerHTML += `
            <tr>
                <td>FB${i+101}</td>
                <td>${f.name}</td>
                <td>${f.dept}</td>
                <td><span class="tag ${f.type.toLowerCase()}">${f.type}</span></td>
                <td><div class="message-box">${f.msg}</div></td>
                <td>${f.date}</td>
                <td>
                    <button class="action-btn" onclick="deleteFeedback(${i})">Delete</button>
                </td>
            </tr>
        `;
    });
}

function deleteFeedback(i){
    if(confirm("Delete this feedback?")){
        feedbacks.splice(i,1);
        localStorage.setItem("feedbacks",JSON.stringify(feedbacks));
        renderTable();
        showToast("Feedback Deleted");
    }
}

function searchFeedback(){
    const val = search.value.toLowerCase();
    const filtered = feedbacks.filter(f =>
        f.name.toLowerCase().includes(val) ||
        f.dept.toLowerCase().includes(val)
    );
    renderTable(filtered);
}

function filterType(type){
    if(!type) renderTable();
    else renderTable(feedbacks.filter(f => f.type === type));
}

function showToast(msg){
    toast.innerText = msg;
    toast.style.display="block";
    setTimeout(()=>toast.style.display="none",2500);
}

renderTable();
