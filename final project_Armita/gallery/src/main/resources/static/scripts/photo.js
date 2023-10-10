document.addEventListener("DOMContentLoaded",()=> {
    const urlParams = new URLSearchParams(window.location.search);
    const photoId = urlParams.get("id");

    getDetails(photoId);

    // Get references to the edit and delete buttons
    const editTitleButton = document.getElementById("editTitleButton");
    const editDescriptionButton = document.getElementById("editDescriptionButton");
    const deleteButton = document.getElementById("deleteButton");

    // Add click event listeners to the edit buttons
    editTitleButton.addEventListener("click", () => {
        handleEditTitle(photoId);
    });

    editDescriptionButton.addEventListener("click", () => {
        handleEditDescription(photoId);
    });

    // Add click event listener to the delete button
    deleteButton.addEventListener("click", () => {
        handleDeletePhoto(photoId);
    });
});

function handleEditDescription(photoId) {
    // Prompt the user for a new description
    const newDescription = prompt("Enter new description:");

    if (newDescription) {
        // Create an object to send as JSON data in the request
        const requestData = {
            description: newDescription
        };

        // Create a new XMLHttpRequest object
        const xhr = new XMLHttpRequest();

        // Configure the PUT request
        xhr.open("PUT", `http://localhost:8080/gallery/update/${photoId}`, true);
        xhr.setRequestHeader("Content-Type", "application/json");

        // Set up a callback function to handle the response
        xhr.onload = function () {
            if (xhr.status === 200) {
                // Description updated successfully
                // Update the displayed description
                document.getElementById("photoDescription").textContent = newDescription;
            } else {
                console.error("Failed to update description:", xhr.status, xhr.statusText);
                // Handle the error here
            }
        };

        // Handle network errors
        xhr.onerror = function () {
            console.error("Network error occurred");
            // Handle network errors here
        };

        // Send the PUT request with JSON data
        xhr.send(JSON.stringify(requestData));
    }
}

function handleDeletePhoto(photoId) {
    // Confirm with the user before proceeding with the deletion
    const confirmDeletion = confirm("Are you sure you want to delete this photo?");

    if (confirmDeletion) {
        // Create a new XMLHttpRequest object
        const xhr = new XMLHttpRequest();

        // Configure the DELETE request
        xhr.open("DELETE", `http://localhost:8080/gallery/delete/${photoId}`, true);

        // Set up a callback function to handle the response
        xhr.onload = function () {
            if (xhr.status === 200) {
                // Photo deleted successfully
                // Redirect the user to the gallery or another page
                window.location.href = "gallery.html";
            } else {
                console.error("Failed to delete photo:", xhr.status, xhr.statusText);
                // Handle the error here
            }
        };

        // Handle network errors
        xhr.onerror = function () {
            console.error("Network error occurred");
            // Handle network errors here
        };

        // Send the DELETE request
        xhr.send();
    }
}

function handleEditTitle(photoId) {
    // Prompt the user for a new title
    const newTitle = prompt("Enter new title:");

    if (newTitle) {
        // Create an object to send as JSON data in the request
        const requestData = {
            title: newTitle
        };

        // Create a new XMLHttpRequest object
        const xhr = new XMLHttpRequest();

        // Configure the PUT request
        xhr.open("PUT", `http://localhost:8080/gallery/update/${photoId}`, true);
        xhr.setRequestHeader("Content-Type", "application/json");

        // Set up a callback function to handle the response
        xhr.onload = function () {
            if (xhr.status === 200) {
                // Title updated successfully
                // Update the displayed title
                document.getElementById("photoTitle").textContent = newTitle;
            } else {
                console.error("Failed to update title:", xhr.status, xhr.statusText);
                // Handle the error here
            }
        };

        // Handle network errors
        xhr.onerror = function () {
            console.error("Network error occurred");
            // Handle network errors here
        };

        // Send the PUT request with JSON data
        xhr.send(JSON.stringify(requestData));
    }
}

    function getDetails(photoId) {
        const photoContainer = document.getElementById("photo-container");
        //retrieve image from
        let xhr = new XMLHttpRequest();
        xhr.open("GET", `http://localhost:8080/gallery/find/${photoId}`, true);
        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                let photoObj = JSON.parse(xhr.responseText);
                document.getElementById("photoTitle").textContent = photoObj['title'];
                document.getElementById("photoDescription").textContent = photoObj['description'];
                //display image
                let photo = document.createElement("img");
                photo.src = "/images/" + photoObj['name'];
                photo.alt = photoObj['description'];
                photoContainer.appendChild(photo);
            }
        };
        // Handle network errors
        xhr.onerror = function () {
            console.error("Network error occurred");
        };
        // Send the AJAX request
        xhr.send();
    }
