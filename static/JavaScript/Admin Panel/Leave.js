// 🔥 REAL DATABASE VERSION - No localStorage!
class LeaveManager {
    constructor() {
        this.rejectId = null;
        this.loadLeaves();  // Auto load on page load
        this.loadStats();   // Load dashboard stats
    }

    // 🔥 1. LOAD LEAVES FROM DATABASE
    async loadLeaves() {
        try {
            const response = await fetch('/admin/api/leaves');
            const leaves = await response.json();
            
            const tableBody = document.getElementById('leaveTable');
            if (!tableBody) return;
            
            tableBody.innerHTML = leaves.map(leave => `
                <tr>
                    <td>LV${leave.id}</td>
                    <td>${leave.empName}</td>
                    <td><span class="badge bg-info">${leave.department}</span></td>
                    <td>${leave.fromDate}</td>
                    <td>${leave.toDate}</td>
                    <td>${leave.reason || '-'}</td>
                    <td>
                        <span class="status-badge ${leave.status.toLowerCase()}">
                            ${leave.status}
                        </span>
                    </td>
                    <td>
                        ${leave.status === "Pending" ? `
                            <button class="action-btn approve me-1" onclick="leaveManager.approve(${leave.id})">
                                ✅ Approve
                            </button>
                            <button class="action-btn reject" onclick="leaveManager.reject(${leave.id})">
                                ❌ Reject
                            </button>
                        ` : `
                            <button class="action-btn delete" onclick="leaveManager.deleteLeave(${leave.id})">
                                🗑️ Delete
                            </button>
                        `}
                    </td>
                </tr>
            `).join('');
        } catch (error) {
            console.error('Error loading leaves:', error);
            this.showToast('Error loading leaves', 'error');
        }
    }

    // 🔥 2. APPROVE LEAVE
    async approve(id) {
        if (!confirm('Approve this leave request?')) return;
        
        try {
            const response = await fetch(`/admin/api/leaves/${id}/approve`, {
                method: 'POST'
            });
            const message = await response.text();
            
            if (response.ok) {
                this.showToast(message, 'success');
                this.loadLeaves();  // Reload table
            } else {
                this.showToast(message, 'error');
            }
        } catch (error) {
            this.showToast('Network error!', 'error');
        }
    }

    // 🔥 3. REJECT LEAVE
    reject(id) {
        this.rejectId = id;
        document.getElementById('rejectReason').value = '';
        const modal = new bootstrap.Modal(document.getElementById('rejectModal'));
        modal.show();
    }

    // 🔥 4. CONFIRM REJECT
    async confirmReject() {
        const reason = document.getElementById('rejectReason').value.trim();
        if (!reason) {
            alert('❌ Rejection reason is required!');
            return;
        }

        try {
            const response = await fetch(`/admin/api/leaves/${this.rejectId}/reject`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `reason=${encodeURIComponent(reason)}`
            });
            
            const message = await response.text();
            if (response.ok) {
                this.showToast(message, 'success');
                bootstrap.Modal.getInstance(document.getElementById('rejectModal')).hide();
                this.loadLeaves();
            } else {
                this.showToast(message, 'error');
            }
        } catch (error) {
            this.showToast('Network error!', 'error');
        }
    }

    // 🔥 5. DELETE LEAVE (Optional)
    async deleteLeave(id) {
        if (!confirm('Delete this leave record?')) return;
        
        // Add delete endpoint in controller if needed
        this.showToast('Delete feature coming soon!', 'info');
    }

    // 🔥 6. LOAD STATS (Dashboard)
    async loadStats() {
        try {
            const response = await fetch('/admin/api/stats');
            const stats = await response.json();
            
            // Update dashboard cards
            document.querySelectorAll('[data-stat]').forEach(el => {
                const key = el.getAttribute('data-stat');
                el.textContent = stats[key] || 0;
            });
        } catch (error) {
            console.error('Error loading stats');
        }
    }

    // 🔥 7. TOAST NOTIFICATION
    showToast(message, type = 'info') {
        const toast = document.getElementById('toast');
        if (!toast) return;
        
        toast.textContent = message;
        toast.className = `toast ${type}`;
        toast.style.display = 'block';
        
        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => toast.style.display = 'none', 300);
        }, 2500);
    }
}

// 🔥 GLOBAL ACCESS
const leaveManager = new LeaveManager();

// 🔥 REJECT BUTTON EVENT
document.addEventListener('DOMContentLoaded', () => {
    const confirmBtn = document.getElementById('confirmReject');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', () => leaveManager.confirmReject());
    }
});

// 🔥 AUTO REFRESH EVERY 30 SECONDS
setInterval(() => leaveManager.loadLeaves(), 30000);