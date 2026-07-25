const API_BASE = "http://localhost:8080";

window.onload = function () {
    loadDevices();
    loadReservations();
};

async function loadDevices() {
    const keyword = document.getElementById("keyword").value.trim();
    const status = document.getElementById("statusFilter").value;

    let url = `${API_BASE}/devices?page=1&size=20`;

    if (keyword !== "") {
        url += `&keyword=${encodeURIComponent(keyword)}`;
    }

    if (status !== "") {
        url += `&status=${encodeURIComponent(status)}`;
    }

    try {
        const response = await fetch(url);
        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        renderDeviceTable(result.data.items);
        showMessage(`Loaded ${result.data.items.length} devices.`, "success");
    } catch (error) {
        showMessage("Failed to load devices.", "error");
    }
}

async function saveDevice() {
    const editingDeviceId = document.getElementById("editingDeviceId").value;
    const name = document.getElementById("deviceName").value.trim();
    const type = document.getElementById("deviceType").value.trim();
    const status = document.getElementById("deviceStatus").value;

    const requestBody = {
        name: name,
        type: type,
        status: status
    };

    const isEditing = editingDeviceId !== "";
    const url = isEditing
        ? `${API_BASE}/devices/${editingDeviceId}`
        : `${API_BASE}/devices`;

    const method = isEditing ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        resetDeviceForm();
        showMessage(isEditing ? "Device updated successfully." : "Device added successfully.", "success");
        loadDevices();
    } catch (error) {
        showMessage("Failed to save device.", "error");
    }
}

function editDevice(id, name, type, status) {
    document.getElementById("editingDeviceId").value = id;
    document.getElementById("deviceName").value = name;
    document.getElementById("deviceType").value = type;
    document.getElementById("deviceStatus").value = status;
    document.getElementById("deviceFormTitle").innerText = "Edit Device";
    showMessage(`Editing device #${id}.`, "info");
}

function resetDeviceForm() {
    document.getElementById("editingDeviceId").value = "";
    document.getElementById("deviceName").value = "";
    document.getElementById("deviceType").value = "";
    document.getElementById("deviceStatus").value = "Available";
    document.getElementById("deviceFormTitle").innerText = "Add Device";
}

async function deleteDevice(id) {
    const confirmed = confirm(`Delete device #${id}?`);

    if (!confirmed) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/devices/${id}`, {
            method: "DELETE"
        });

        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        showMessage("Device deleted successfully.", "success");
        loadDevices();
    } catch (error) {
        showMessage("Failed to delete device.", "error");
    }
}

async function loadReservations() {
    const url = `${API_BASE}/reservations?page=1&size=20`;

    try {
        const response = await fetch(url);
        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        renderReservationTable(result.data.items);
        showMessage(`Loaded ${result.data.items.length} reservations.`, "success");
    } catch (error) {
        showMessage("Failed to load reservations.", "error");
    }
}

async function addReservation() {
    const deviceId = Number(document.getElementById("reservationDeviceId").value);
    const userName = document.getElementById("reservationUserName").value.trim();
    const reservationDate = document.getElementById("reservationDate").value;
    const startTime = document.getElementById("reservationStartTime").value;
    const endTime = document.getElementById("reservationEndTime").value;

    const requestBody = {
        deviceId: deviceId,
        userName: userName,
        reservationDate: reservationDate,
        startTime: startTime,
        endTime: endTime
    };

    try {
        const response = await fetch(`${API_BASE}/reservations`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        document.getElementById("reservationDeviceId").value = "";
        document.getElementById("reservationUserName").value = "";
        document.getElementById("reservationDate").value = "";
        document.getElementById("reservationStartTime").value = "";
        document.getElementById("reservationEndTime").value = "";

        showMessage("Reservation added successfully.", "success");
        loadReservations();
    } catch (error) {
        showMessage("Failed to add reservation.", "error");
    }
}

async function deleteReservation(id) {
    const confirmed = confirm(`Delete reservation #${id}?`);

    if (!confirmed) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/reservations/${id}`, {
            method: "DELETE"
        });

        const result = await response.json();

        if (result.code !== 200) {
            showMessage(result.message, "error");
            return;
        }

        showMessage("Reservation deleted successfully.", "success");
        loadReservations();
    } catch (error) {
        showMessage("Failed to delete reservation.", "error");
    }
}

function renderDeviceTable(devices) {
    const tableBody = document.getElementById("deviceTableBody");
    tableBody.innerHTML = "";

    for (const device of devices) {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${device.id}</td>
            <td>${device.name}</td>
            <td>${device.type}</td>
            <td><span class="status ${device.status}">${device.status}</span></td>
            <td>
                <button class="secondary" onclick="editDevice(${device.id}, '${escapeText(device.name)}', '${escapeText(device.type)}', '${escapeText(device.status)}')">Edit</button>
                <button class="danger" onclick="deleteDevice(${device.id})">Delete</button>
            </td>
        `;

        tableBody.appendChild(row);
    }
}

function renderReservationTable(reservations) {
    const tableBody = document.getElementById("reservationTableBody");
    tableBody.innerHTML = "";

    for (const reservation of reservations) {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${reservation.id}</td>
            <td>${reservation.deviceId}</td>
            <td>${reservation.userName}</td>
            <td>${reservation.reservationDate}</td>
            <td>${reservation.startTime}</td>
            <td>${reservation.endTime}</td>
            <td>
                <button class="danger" onclick="deleteReservation(${reservation.id})">Delete</button>
            </td>
        `;

        tableBody.appendChild(row);
    }
}

function showMessage(message, type) {
    const messageElement = document.getElementById("message");
    messageElement.innerText = message;
    messageElement.className = type || "info";
}

function escapeText(value) {
    return String(value)
        .replaceAll("\\", "\\\\")
        .replaceAll("'", "\\'")
        .replaceAll('"', "&quot;");
}