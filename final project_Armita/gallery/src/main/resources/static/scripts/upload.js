const title = document.getElementById("imageTitle");
const description = document.getElementById("imageDescription");
const imgFile = document.getElementById("imageFile");
const form = document.getElementById("imageUploadForm");
const myModal = new bootstrap.Modal(document.getElementById('myModal'));

function uploadPhotoToGallery(callback) {
    const xhr = new XMLHttpRequest();
    let formData = new FormData();
    formData.append("title", title.value)
    formData.append("description", description.value)
    formData.append("name", imgFile.files[0]); // Get the selected image file

    xhr.open("POST", "http://localhost:8080/gallery/add", true);
    xhr.onload = function () {
        const response = {
            status: xhr.status,
            data: xhr.responseText
        };

        callback(response);
    }
    // Handle network errors
    xhr.onerror = function () {
        console.error("Network error occurred");
        // Handle network errors here
    };
    xhr.send(formData);
}

function init() {
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        const modal_title = document.getElementById("modal-title");
        const modal_body = document.getElementById("modal-body");

        uploadPhotoToGallery((response) => {
            if (response.status === 200) {
                modal_title.innerHTML = "Success";
                modal_body.innerHTML = response.data;
                myModal.show();

            } else {
                modal_title.innerHTML = "Failed";
                modal_body.innerHTML = `Error: ${response.status}\n${response.data}`;
                myModal.show();
            }
        });
        form.reset();
    })
}




