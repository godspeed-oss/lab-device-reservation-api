async function addReservation() {
    if (!ensureLoggedIn()) {
        return;
    }

    const deviceId = Number(document.getElementById("reservationDeviceId").value);
    const reservationDate = document.getElementById("reservationDate").value;
    const startTime = document.getElementById("reservationStartTime").value;
    const endTime = document.getElementById("reservationEndTime").value;

    const requestBody = {
        deviceId: deviceId,
        userName: currentUser.username,
        reservationDate: reservationDate,
        startTime: startTime,
        endTime: endTime
    };

    try {
        const response = await fetch(`${API_BASE}/reservations`, {
            method: "POST",
            headers: getJsonHeaders(),
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