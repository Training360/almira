var defaultSize = 10;
var state = "default";

window.onload = function() {
    downloadLocations();
    var locationForm = document.getElementById("location-form");
    locationForm.onsubmit = createLocation;

    var updateLocationForm = document.getElementById("update-location-form");
    updateLocationForm.onsubmit = updateLocation;

    var prevLink = document.getElementById("prev-link");
    prevLink.onclick = doPaging;
    var nextLink = document.getElementById("next-link");
    nextLink.onclick = doPaging;

    document.getElementById("update-cancel-button").onclick = cancelUpdate;

    document.getElementById("create-cancel-button").onclick = cancelCreate;

    document.getElementById("create-location-link").onclick = prepareCreateLocation;

    updateWebservicesLink();
};

function updateWebservicesLink() {
    let address = window.location + "/services";
    link = document.getElementById("webservices-link")
    link.href = address;
    link.innerHTML = address;
}

function setState(pState) {
    state = pState;
    if (state == "default") {
        document.getElementById("location-name").value = "";
        document.getElementById("location-coords").value = "";
        document.getElementById("create-location-link").removeAttribute("hidden");
        document.getElementById("location-form").setAttribute("hidden", "hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "create") {
        document.getElementById("create-location-link").setAttribute("hidden", "hidden");
        document.getElementById("location-form").removeAttribute("hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "update") {
            document.getElementById("create-location-link").setAttribute("hidden", "hidden");
            document.getElementById("location-form").setAttribute("hidden", "hidden");
            document.getElementById("update-location-form").removeAttribute("hidden");
    }
}

function prepareCreateLocation() {
    setState("create");
    return false;
}

function cancelUpdate() {
    setState("default");
}

function cancelCreate() {
    document.getElementById("location-form").setAttribute("hidden", "hidden");
    document.getElementById("create-location-link").removeAttribute("hidden");
}

function doPaging() {
    link = this.getAttribute("href");
    downloadLocations(getParameterByName("start", link), getParameterByName("size", link))
    return false;
}

function downloadLocations(start = 0, size) {
    if (!size) {
        size = defaultSize;
    }
    let url = 'api/locations?start=' + start + "&size=" + size;

    fetch(url)
        .then(function(response) {
            return response.json();
            })
        .then(function(jsonData) {
            initPagination(jsonData.start, jsonData.count);
            fillTable(jsonData.locations);
        });
}

function fillTable(locations) {
  var locationsTable = document.getElementById("locations-table");
  locationsTable.innerHTML = "";
  for (var i = 0; i < locations.length; i++) {
    var tr = document.createElement("tr");
    tr["raw-data"] = locations[i];

    var idTd = document.createElement("td");
    idTd.innerHTML = locations[i].id;
    tr.appendChild(idTd);

    var nameTd = document.createElement("td");
    nameTd.innerHTML = locations[i].name;
    tr.appendChild(nameTd);

    var coordsTd = document.createElement("td");
    coordsTd.innerHTML = locations[i].lat + ", " + locations[i].lon;
    tr.appendChild(coordsTd);

    var buttonsTd = document.createElement("td");
    var editButton = document.createElement("button");
    editButton.setAttribute("class", "btn btn-link");
    editButton.innerHTML= "Edit";
    editButton.onclick = prepareForUpdateLocation;
    buttonsTd.appendChild(editButton);

    var deleteButton = document.createElement("button");
    deleteButton.setAttribute("class", "btn btn-danger");
    deleteButton.innerHTML= "Delete";
    deleteButton.onclick = deleteLocation;
    buttonsTd.appendChild(deleteButton);

    tr.appendChild(buttonsTd);

    locationsTable.appendChild(tr);
  }
}

function initPagination(start, count) {
    if (start == 0) {
        document.getElementById("prev-link").setAttribute("hidden", "hidden");
    }
    else {
        document.getElementById("prev-link").removeAttribute("hidden");
        document.getElementById("prev-link").setAttribute("href", "?start=" + (start - defaultSize) + "&size=" + defaultSize)
    }
    document.getElementById("start-span").innerHTML = start;
    if (start + defaultSize < count) {
            document.getElementById("next-link").removeAttribute("hidden");
            document.getElementById("next-link").setAttribute("href", "?start=" + (start + defaultSize) + "&size=" + defaultSize)
        }
        else {
            document.getElementById("next-link").setAttribute("hidden", "hidden");
        }
}

function prepareForUpdateLocation() {
    setState("update");

    var location = this.parentElement.parentElement["raw-data"];
    document.getElementById("update-location-id").value = location.id;
    document.getElementById("update-location-name").value = location.name;
    document.getElementById("update-location-coords").value = location.lat + "," + location.lon;
}

function createLocation() {
    var name = document.getElementById("location-name").value;
    var coords = document.getElementById("location-coords").value;

    var request = {"name": name, "coords": coords};


    fetch("api/locations", {
        method: "POST",
        body: JSON.stringify(request),
        headers: {
            "Content-type": "application/json"
        }
    })
    .then(function(response) {
        return response.json().then(function(jsonData) {
            return {status: response.status, body: jsonData}
        });
    })
    .then(function(jsonData) {
        if (jsonData.status == 200) {
           successCreate();
        }
        else {
            document.getElementById("message-div").innerHTML = jsonData.body.message;
            document.getElementById("message-div").setAttribute("class", "alert alert-danger");
        }
    });

    return false;
}

function successCreate() {
    console.log("Successfull post");
    document.getElementById("location-name").value = "";
    document.getElementById("location-coords").value = "";
    downloadLocations();
    setState("default");
    document.getElementById("message-div").innerHTML = "Location has created";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function deleteLocation() {
    setState("default");
    if (!confirm("Are you sure to delete?")) {
        return;
    }
    var location = this.parentElement.parentElement["raw-data"];
    var url = 'api/locations/' + location.id;

    fetch(url, {
            method: "DELETE"
        })
        .then(function(response) {
            successDelete();
        })
        return false;
}

function successDelete() {
    console.log("Successfull delete");
    downloadLocations();
    document.getElementById("message-div").innerHTML = "Location has deleted";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function updateLocation() {
    var id = document.getElementById("update-location-id").value;
    var name = document.getElementById("update-location-name").value;
    var coords = document.getElementById("update-location-coords").value;

    var request = {"name": name, "coords": coords};

    var url = 'api/locations/' + id;

    fetch(url, {
        method: "POST",
        body: JSON.stringify(request),
        headers: {
            "Content-type": "application/json"
        }
    })
    .then(function(response) {
        return response.json().then(function(jsonData) {
            return {status: response.status, body: jsonData}
        });
    })
    .then(function(jsonData) {
        if (jsonData.status == 200) {
           successUpdate();
        }
        else {
            document.getElementById("message-div").innerHTML = jsonData.body.message;
            document.getElementById("message-div").setAttribute("class", "alert alert-danger");
        }
    });

    return false;
}

function successUpdate() {
    console.log("Successfull update");
    document.getElementById("update-location-id").value = "";
    document.getElementById("update-location-name").value = "";
    document.getElementById("update-location-coords").value = "";
    var start = document.getElementById("start-span").innerHTML;
    downloadLocations(start);
    setState("default");
    document.getElementById("message-div").innerHTML = "Location has modified";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}