function init() {

    const btn = document.getElementById("add-photo-btn");
    btn.addEventListener("click",
        function () {
            window.location.href = "upload.html"
        });


    const galleryContainer = document.getElementById("photoGallery");

    // Create a new XMLHttpRequest object
    const xhr = new XMLHttpRequest();

    // Configure the request
    xhr.open("GET", "http://localhost:8080/gallery/all", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    // Set up a callback function to handle the response
    xhr.onload = function () {
        if (xhr.status === 200) {
            const photos = JSON.parse(xhr.responseText);


            // Calculate the number of columns and rows based on the number of photos
            const numPhotos = photos.length;
            const numColumns = Math.ceil(Math.sqrt(numPhotos));
            const numRows = Math.ceil(numPhotos / numColumns);

            // Loop through photos and create the gallery
            for (let row = 0; row < numRows; row++) {
                const rowDiv = document.createElement("div");
                rowDiv.classList.add("row");
                rowDiv.style.height = Math.floor(90 / numRows) + "vh";
                console.log(rowDiv.style.height);

                for (let col = 0; col < numColumns; col++) {
                    const index = row * numColumns + col;
                    if (index < numPhotos) {

                        const colDiv = document.createElement("div");
                        colDiv.classList.add("col");

                        // Create an anchor element and set the href attribute
                        const link = document.createElement("a");
                        // link.href = "photo.html";

                        // Create the image element
                        const img = document.createElement("img");
                        img.src = "/images/" + photos[index]['name'];
                        img.alt = photos[index]['description'];
                        img.onclick = function () {
                            window.location.href = `../photo.html?id=${photos[index]['id']}`
                        };

                        // Append the image to the anchor element
                        link.appendChild(img);

                        // Append the anchor element to the column
                        colDiv.appendChild(link);

                        // Append the column to the row
                        rowDiv.appendChild(colDiv);

                    }
                }
                galleryContainer.appendChild(rowDiv);
            }
        } else {
            console.error("API error:", xhr.status, xhr.statusText);
        }
    };
    // Handle network errors
    xhr.onerror = function () {
        console.error("Network error occurred");
    };

    // Send the AJAX request
    xhr.send();
}


