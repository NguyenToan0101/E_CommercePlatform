// Navigation functionality
document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.nav-item');
    const pages = document.querySelectorAll('.page');
    const pageTitle = document.getElementById('page-title');

    // Page titles mapping
    const pageTitles = {
        'dashboard': 'Dashboard',
        'products': 'Products',
        'orders': 'Orders',
        'inventory': 'Inventory Management',
        'customers': 'Customers',
        'messages': 'Customer Messages',
        'marketing': 'Marketing',
        'analytics': 'Analytics',
        'finance': 'Finance',
        'settings': 'Settings'
    };

    // Handle navigation clicks
    navItems.forEach(item => {
        item.addEventListener('click', function() {
            const targetPage = this.getAttribute('data-page');

            // Remove active class from all nav items
            navItems.forEach(nav => nav.classList.remove('active'));

            // Add active class to clicked item
            this.classList.add('active');

            // Hide all pages
            pages.forEach(page => page.classList.remove('active'));

            // Show target page
            const targetPageElement = document.getElementById(targetPage + '-page');
            if (targetPageElement) {
                targetPageElement.classList.add('active');
            }

            // Update page title
            pageTitle.textContent = pageTitles[targetPage] || 'Dashboard';

            // Initialize page-specific functionality
            if (targetPage === 'analytics') {
                setTimeout(initializeAnalyticsChart, 100);
            } else if (targetPage === 'finance') {
                setTimeout(() => {
                    initializeRevenueChart();
                    initializeCategoryChart();
                }, 100);
            }
        });
    });

    // Initialize all functionality
    initializeSalesChart();
    initializeSearch();
    initializeFilters();
    initializeTabs();
    initializeMessages();
    initializeMarketing();
    initializeSettings();
    handleMobileNavigation();
    simulateRealTimeUpdates();
});

// Sales Chart
function initializeSalesChart() {
    const ctx = document.getElementById('salesChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Sales',
                data: [2800, 3200, 1400, 1200, 1800, 2000, 5000, 2600, 2400, 4000, 3800, 6800],
                backgroundColor: '#4ecdc4',
                borderColor: '#44a08d',
                borderWidth: 1,
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: '#f3f4f6'
                    },
                    ticks: {
                        callback: function(value) {
                            return '$' + value;
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

// Analytics Chart
function initializeAnalyticsChart() {
    const ctx = document.getElementById('analyticsChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Revenue',
                data: [3000, 2800, 3200, 3500, 4000, 3800, 4200, 4500, 4800, 5200, 5500, 6000],
                borderColor: '#ff5722',
                backgroundColor: 'rgba(255, 87, 34, 0.1)',
                tension: 0.4,
                fill: true
            }, {
                label: 'Orders',
                data: [150, 140, 160, 175, 200, 190, 210, 225, 240, 260, 275, 300],
                borderColor: '#2196f3',
                backgroundColor: 'rgba(33, 150, 243, 0.1)',
                tension: 0.4,
                fill: true,
                yAxisID: 'y1'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            scales: {
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    grid: {
                        color: '#f3f4f6'
                    },
                    ticks: {
                        callback: function(value) {
                            return '$' + value;
                        }
                    }
                },
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    grid: {
                        drawOnChartArea: false,
                    },
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                }
            }
        }
    });
}

// Revenue Chart
function initializeRevenueChart() {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Revenue',
                data: [15000, 12000, 18000, 22000, 25000, 28000, 32000, 35000, 38000, 42000, 45000, 48000],
                borderColor: '#ff5722',
                backgroundColor: 'rgba(255, 87, 34, 0.1)',
                tension: 0.4,
                fill: true
            }, {
                label: 'Expenses',
                data: [8000, 7500, 9000, 10000, 11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000],
                borderColor: '#f44336',
                backgroundColor: 'rgba(244, 67, 54, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: '#f3f4f6'
                    },
                    ticks: {
                        callback: function(value) {
                            return '$' + value.toLocaleString();
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                }
            }
        }
    });
}

// Category Chart
function initializeCategoryChart() {
    const ctx = document.getElementById('categoryChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Electronics', 'Clothing', 'Home & Kitchen', 'Beauty', 'Sports'],
            datasets: [{
                data: [45000, 25000, 18000, 12000, 8000],
                backgroundColor: [
                    '#ff5722',
                    '#2196f3',
                    '#4caf50',
                    '#9c27b0',
                    '#ff9800'
                ],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

// Search functionality
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-box input');

    searchInputs.forEach(input => {
        input.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const table = this.closest('.page').querySelector('.data-table tbody');

            if (table) {
                const rows = table.querySelectorAll('tr');
                rows.forEach(row => {
                    const text = row.textContent.toLowerCase();
                    row.style.display = text.includes(searchTerm) ? '' : 'none';
                });
            }

            // Search in conversations
            const conversationsList = this.closest('.messages-sidebar')?.querySelector('.conversations-list');
            if (conversationsList) {
                const conversations = conversationsList.querySelectorAll('.conversation-item');
                conversations.forEach(conv => {
                    const text = conv.textContent.toLowerCase();
                    conv.style.display = text.includes(searchTerm) ? '' : 'none';
                });
            }
        });
    });
}

// Filter functionality
function initializeFilters() {
    const filterSelects = document.querySelectorAll('.filter-select');
    const filterBtns = document.querySelectorAll('.filter-btn');

    // Dropdown filters
    filterSelects.forEach(select => {
        select.addEventListener('change', function() {
            const filterValue = this.value.toLowerCase();
            const table = this.closest('.page').querySelector('.data-table tbody');

            if (table) {
                const rows = table.querySelectorAll('tr');
                rows.forEach(row => {
                    if (filterValue.includes('all') || filterValue === '') {
                        row.style.display = '';
                    } else {
                        const statusCell = row.querySelector('.status');
                        if (statusCell) {
                            const status = statusCell.textContent.toLowerCase();
                            row.style.display = status.includes(filterValue) ? '' : 'none';
                        }
                    }
                });
            }
        });
    });

    // Button filters
    filterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const parentContainer = this.closest('.page, .messages-sidebar');
            if (!parentContainer) return;

            // Remove active class from all filter buttons in the same container
            parentContainer.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));

            // Add active class to clicked button
            this.classList.add('active');

            const filterValue = this.textContent.toLowerCase();

            // Handle inventory filters
            const table = parentContainer.querySelector('.data-table tbody');
            if (table) {
                const rows = table.querySelectorAll('tr');
                rows.forEach(row => {
                    if (filterValue === 'all products') {
                        row.style.display = '';
                    } else {
                        const statusCell = row.querySelector('.status');
                        if (statusCell) {
                            const status = statusCell.textContent.toLowerCase();
                            row.style.display = status.includes(filterValue) ? '' : 'none';
                        }
                    }
                });
            }

            // Handle message filters
            const conversationsList = parentContainer.querySelector('.conversations-list');
            if (conversationsList) {
                const conversations = conversationsList.querySelectorAll('.conversation-item');
                conversations.forEach(conv => {
                    if (filterValue === 'all') {
                        conv.style.display = '';
                    } else if (filterValue === 'unread') {
                        const hasUnread = conv.querySelector('.unread-indicator');
                        conv.style.display = hasUnread ? '' : 'none';
                    } else if (filterValue === 'archived') {
                        // Simulate archived conversations
                        conv.style.display = Math.random() > 0.7 ? '' : 'none';
                    }
                });
            }
        });
    });
}

// Tab functionality
function initializeTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');
            const parentContainer = this.closest('.page, .transaction-section');

            if (!parentContainer) return;

            // Remove active class from all tabs in this container
            parentContainer.querySelectorAll('.tab-btn').forEach(tab => tab.classList.remove('active'));
            parentContainer.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

            // Add active class to clicked tab
            this.classList.add('active');

            // Show target content
            const targetContent = parentContainer.querySelector(`#${targetTab}-tab`);
            if (targetContent) {
                targetContent.classList.add('active');
            }
        });
    });
}

// Messages functionality
function initializeMessages() {
    const conversationItems = document.querySelectorAll('.conversation-item');
    const messageInput = document.querySelector('.message-input');
    const sendBtn = document.querySelector('.send-btn');
    const chatMessages = document.querySelector('.chat-messages');

    // Handle conversation selection
    conversationItems.forEach(item => {
        item.addEventListener('click', function() {
            conversationItems.forEach(conv => conv.classList.remove('active'));
            this.classList.add('active');

            // Remove unread indicator
            const unreadIndicator = this.querySelector('.unread-indicator');
            if (unreadIndicator) {
                unreadIndicator.remove();
                updateUnreadCount();
            }

            // Update chat header with selected customer info
            const customerName = this.querySelector('.customer-name').textContent;
            const chatCustomerName = document.querySelector('.chat-customer-info h3');
            if (chatCustomerName) {
                chatCustomerName.textContent = customerName;
            }

            // Load conversation history (simulate)
            loadConversationHistory(customerName);
        });
    });

    // Handle sending messages
    function sendMessage() {
        const messageText = messageInput.value.trim();
        if (!messageText) return;

        // Create new message element
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message seller-message';
        messageDiv.innerHTML = `
            <div class="message-content">${messageText}</div>
            <div class="message-time">Just now</div>
        `;

        // Add to chat
        if (chatMessages) {
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        // Clear input
        messageInput.value = '';

        // Show typing indicator (simulate customer response)
        setTimeout(() => {
            const responses = [
                "Thank you for your response!",
                "That's very helpful, thanks!",
                "I appreciate your quick reply.",
                "Perfect, that answers my question.",
                "Great, I'll wait for the update."
            ];

            const randomResponse = responses[Math.floor(Math.random() * responses.length)];

            const typingDiv = document.createElement('div');
            typingDiv.className = 'message customer-message';
            typingDiv.innerHTML = `
                <div class="message-content">${randomResponse}</div>
                <div class="message-time">Just now</div>
            `;

            if (chatMessages) {
                chatMessages.appendChild(typingDiv);
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }, 1000 + Math.random() * 2000);
    }

    if (sendBtn) {
        sendBtn.addEventListener('click', sendMessage);
    }

    if (messageInput) {
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });
    }
}

// Load conversation history
function loadConversationHistory(customerName) {
    const chatMessages = document.querySelector('.chat-messages');
    if (!chatMessages) return;

    // Clear current messages
    chatMessages.innerHTML = '';

    // Simulate different conversation histories
    const conversations = {
        'Sarah Johnson': [
            { type: 'customer', content: 'Hi, I have a question about my recent order #ORD-7651', time: '32 minutes ago' },
            { type: 'seller', content: "Hello! I'd be happy to help you with your order. What would you like to know?", time: '27 minutes ago' },
            { type: 'customer', content: 'I was wondering when it will be shipped. I placed the order yesterday.', time: '22 minutes ago' },
            { type: 'seller', content: "Let me check that for you. Your order is currently being processed and will be shipped within 24 hours. You'll receive a tracking number via email once it's dispatched.", time: '17 minutes ago' },
            { type: 'customer', content: "That's great! Thank you for the quick response.", time: '12 minutes ago' },
            { type: 'seller', content: "You're welcome! Is there anything else I can help you with?", time: '7 minutes ago' }
        ],
        'Michael Brown': [
            { type: 'customer', content: 'When will my order be shipped?', time: '32 minutes ago' },
            { type: 'seller', content: 'Your order will be shipped within 2-3 business days.', time: '30 minutes ago' },
            { type: 'customer', content: 'Thank you for the information!', time: '28 minutes ago' }
        ],
        'Emily Davis': [
            { type: 'customer', content: 'I received my order and I love it!', time: '2 hours ago' },
            { type: 'seller', content: "That's wonderful to hear! Thank you for your feedback.", time: '2 hours ago' },
            { type: 'customer', content: 'Thank you for the quick response!', time: '2 hours ago' }
        ]
    };

    const messages = conversations[customerName] || [
        { type: 'customer', content: 'Hello, I need help with my order.', time: '1 hour ago' },
        { type: 'seller', content: 'Hi! I\'m here to help. What can I assist you with?', time: '58 minutes ago' }
    ];

    messages.forEach(msg => {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${msg.type}-message`;
        messageDiv.innerHTML = `
            <div class="message-content">${msg.content}</div>
            <div class="message-time">${msg.time}</div>
        `;
        chatMessages.appendChild(messageDiv);
    });

    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Update unread count
function updateUnreadCount() {
    const unreadCount = document.querySelector('.unread-count');
    const unreadIndicators = document.querySelectorAll('.unread-indicator');

    if (unreadCount) {
        unreadCount.textContent = unreadIndicators.length;
    }
}

// Marketing tools functionality
function initializeMarketing() {
    const toolCards = document.querySelectorAll('.tool-card button');

    toolCards.forEach(btn => {
        btn.addEventListener('click', function() {
            const toolName = this.closest('.tool-card').querySelector('h3').textContent;

            // Simulate different actions for different tools
            switch(toolName) {
                case 'Email Marketing':
                    showModal('Create Email Campaign', createEmailForm());
                    break;
                case 'Discount Codes':
                    showModal('Create Discount Code', createDiscountForm());
                    break;
                case 'Social Media':
                    showNotification('Redirecting to social media management...', 'info');
                    break;
                case 'SEO Tools':
                    showModal('SEO Optimization', createSEOForm());
                    break;
                default:
                    showNotification(`${toolName} feature would be implemented here`, 'info');
            }
        });
    });
}

// Settings form handling
function initializeSettings() {
    const settingsForms = document.querySelectorAll('.settings-form');

    settingsForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;

            submitBtn.textContent = 'Saving...';
            submitBtn.disabled = true;

            // Simulate API call
            setTimeout(() => {
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
                showNotification('Settings saved successfully!', 'success');

                // Update UI with new values if needed
                updateSettingsUI(this);
            }, 1500);
        });
    });

    // Handle logo upload
    const logoUploadBtns = document.querySelectorAll('.logo-upload button');
    logoUploadBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const input = document.createElement('input');
            input.type = 'file';
            input.accept = 'image/*';
            input.onchange = function(e) {
                const file = e.target.files[0];
                if (file) {
                    showNotification('Logo uploaded successfully!', 'success');
                    // Here you would typically upload the file to your server
                }
            };
            input.click();
        });
    });
}

// Update settings UI
function updateSettingsUI(form) {
    const formData = new FormData(form);

    // Update shop name in header if changed
    const shopName = formData.get('shop-name');
    if (shopName) {
        const logoText = document.querySelector('.logo span');
        if (logoText) {
            logoText.textContent = shopName;
        }
    }
}

// Mobile navigation
function handleMobileNavigation() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    if (window.innerWidth <= 768) {
        // Create mobile menu button if it doesn't exist
        let mobileMenuBtn = document.querySelector('.mobile-menu-btn');
        if (!mobileMenuBtn) {
            mobileMenuBtn = document.createElement('button');
            mobileMenuBtn.className = 'mobile-menu-btn';
            mobileMenuBtn.innerHTML = '<i class="fas fa-bars"></i>';
            mobileMenuBtn.style.cssText = `
                position: fixed;
                top: 1rem;
                left: 1rem;
                z-index: 1001;
                background: #ff5722;
                color: white;
                border: none;
                padding: 0.75rem;
                border-radius: 0.375rem;
                cursor: pointer;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            `;

            document.body.appendChild(mobileMenuBtn);

            mobileMenuBtn.addEventListener('click', function() {
                const isOpen = sidebar.style.transform === 'translateX(0px)';
                sidebar.style.transform = isOpen ? 'translateX(-100%)' : 'translateX(0px)';
            });
        }

        // Close sidebar when clicking outside
        mainContent.addEventListener('click', function() {
            if (sidebar.style.transform === 'translateX(0px)') {
                sidebar.style.transform = 'translateX(-100%)';
            }
        });
    } else {
        // Remove mobile menu button on larger screens
        const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
        if (mobileMenuBtn) {
            mobileMenuBtn.remove();
        }
    }
}

window.addEventListener('resize', handleMobileNavigation);
handleMobileNavigation();

// Utility functions for dynamic content
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    }).format(new Date(date));
}

// Simulate real-time updates
function simulateRealTimeUpdates() {
    // Update unread message count
    const unreadCount = document.querySelector('.unread-count');
    if (unreadCount) {
        let count = parseInt(unreadCount.textContent);
        setInterval(() => {
            if (Math.random() > 0.7) { // 30% chance of new message
                count++;
                unreadCount.textContent = count;

                // Add notification
                showNotification('New message received', 'info');
            }
        }, 30000); // Check every 30 seconds
    }

    // Update stats periodically
    setInterval(() => {
        const statValues = document.querySelectorAll('.stat-value');
        statValues.forEach(stat => {
            if (stat.textContent.includes('$')) {
                // Simulate small revenue changes
                const currentValue = parseFloat(stat.textContent.replace(/[$,]/g, ''));
                const change = (Math.random() - 0.5) * 100; // Random change ±$50
                const newValue = Math.max(0, currentValue + change);
                stat.textContent = '$' + newValue.toLocaleString('en-US', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                });
            }
        });
    }, 60000); // Update every minute
}

// Start real-time updates
setTimeout(simulateRealTimeUpdates, 5000);

// Modal functionality
function showModal(title, content) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close-btn">&times;</span>
            <h2>${title}</h2>
            ${content}
        </div>
    `;

    document.body.appendChild(modal);

    const closeBtn = modal.querySelector('.close-btn');
    closeBtn.addEventListener('click', function() {
        document.body.removeChild(modal);
    });

    modal.style.display = 'block';
}

// Form creation functions
function createEmailForm() {
    return `
        <form>
            <label for="email-subject">Subject:</label>
            <input type="text" id="email-subject" name="email-subject">
            <label for="email-body">Body:</label>
            <textarea id="email-body" name="email-body"></textarea>
            <button type="submit">Send</button>
        </form>
    `;
}

function createDiscountForm() {
    return `
        <form>
            <label for="discount-code">Code:</label>
            <input type="text" id="discount-code" name="discount-code">
            <label for="discount-value">Value:</label>
            <input type="number" id="discount-value" name="discount-value">
            <button type="submit">Create</button>
        </form>
    `;
}

function createSEOForm() {
    return `
        <form>
            <label for="seo-keywords">Keywords:</label>
            <input type="text" id="seo-keywords" name="seo-keywords">
            <label for="seo-description">Description:</label>
            <textarea id="seo-description" name="seo-description"></textarea>
            <button type="submit">Optimize</button>
        </form>
    `;
}

// Notification system
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;

    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 1.5rem;
        background: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#3b82f6'};
        color: white;
        border-radius: 0.5rem;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        z-index: 1000;
        transform: translateX(100%);
        transition: transform 0.3s ease;
    `;

    document.body.appendChild(notification);

    // Animate in
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);

    // Remove after 3 seconds
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Add click handlers for action buttons
document.addEventListener('click', function(e) {
    if (e.target.matches('.btn-icon')) {
        e.preventDefault();
        const icon = e.target.querySelector('i');
        if (icon && icon.classList.contains('fa-edit')) {
            showNotification('Edit functionality would be implemented here', 'info');
        } else if (icon && icon.classList.contains('fa-trash')) {
            if (confirm('Are you sure you want to delete this item?')) {
                showNotification('Item deleted successfully', 'success');
                // Remove the row
                const row = e.target.closest('tr');
                if (row) {
                    row.style.opacity = '0';
                    setTimeout(() => row.remove(), 300);
                }
            }
        }
    }

    if (e.target.matches('.btn-link')) {
        e.preventDefault();
        showNotification('Detail view would be implemented here', 'info');
    }
});