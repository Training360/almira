var defaultSize = 10;
var state = "default";

window.onload = function() {
    fillTable();
    var locationForm = document.getElementById("location-form");
    locationForm.onsubmit = createLocation;

    var updateLocationForm = document.getElementById("update-location-form");
    updateLocationForm.onsubmit = updateLocation;

    var prevLink = document.getElementById("prev-link");
    prevLink.onclick = fillTableAt;
    var nextLink = document.getElementById("next-link");
    nextLink.onclick = fillTableAt;

    document.getElementById("update-cancel-button").onclick = cancelUpdate;

    document.getElementById("create-cancel-button").onclick = cancelCreate;

    document.getElementById("create-location-link").onclick = prepareCreateLocation;
};

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
}

function cancelUpdate() {
    setState("default");
}

function cancelCreate() {
    document.getElementById("location-form").setAttribute("hidden", "hidden");
    document.getElementById("create-location-link").removeAttribute("hidden");
}

function fillTableAt() {
    link = this.getAttribute("href");
    fillTable(getParameterByName("start", link), getParameterByName("size", link))
    return false;
}



function fillTable(start = 0, size) {
    if (!size) {
        size = defaultSize;
    }
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'api/locations?start=' + start + "&size=" + size);
    xhr.onreadystatechange = function () {
      var DONE = 4;
      var OK = 200;
      if (xhr.readyState === DONE) {
        if (xhr.status === OK) {
          var response = JSON.parse(xhr.responseText);
          initPagination(response.start, response.count);
          var locations = response.locations;
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
        } else {
          console.log('Error: ' + xhr.status);
        }
      }
    };
    xhr.send(null);

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

    var req = {"name": name, "coords": coords};

    var xhr = new XMLHttpRequest();
        xhr.open('POST', 'api/locations');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function () {
          var DONE = 4;
          var OK = 200;
          if (xhr.readyState === DONE) {
            if (xhr.status === OK) {
                console.log("Successfull post");
                document.getElementById("location-name").value = "";
                document.getElementById("location-coords").value = "";
                fillTable();
                setState("default");
                document.getElementById("message-div").innerHTML = "Location has created";
                document.getElementById("message-div").setAttribute("class", "alert alert-success");
            } else if (xhr.status == 400){
                var response = JSON.parse(xhr.responseText);
                document.getElementById("message-div").innerHTML = response.message;
                document.getElementById("message-div").setAttribute("class", "alert alert-danger");

            } else {
              console.log('Error: ' + xhr.responseText);
            }
          }
        };
        xhr.send(JSON.stringify(req));
        return false;

}

function deleteLocation() {
    setState("default");
    if (!confirm("Are you sure to delete?")) {
        return;
    }
    var location = this.parentElement.parentElement["raw-data"];
    var xhr = new XMLHttpRequest();
        xhr.open('DELETE', 'api/locations/' + location.id);
        xhr.onreadystatechange = function () {
          var DONE = 4;
          var OK = 200;
          if (xhr.readyState === DONE) {
            if (xhr.status === OK) {
                console.log("Successfull delete");
                fillTable();
                document.getElementById("message-div").innerHTML = "Location has deleted";
                document.getElementById("message-div").setAttribute("class", "alert alert-success");
            } else {
              console.log('Error: ' + xhr.responseText);
            }
          }
        };
        xhr.send();
        return false;

}

function updateLocation() {
    var id = document.getElementById("update-location-id").value;
    var name = document.getElementById("update-location-name").value;
    var coords = document.getElementById("update-location-coords").value;

    var req = {"name": name, "coords": coords};

    var xhr = new XMLHttpRequest();
        xhr.open('POST', 'api/locations/' + id);
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function () {
          var DONE = 4;
          var OK = 200;
          if (xhr.readyState === DONE) {
            if (xhr.status === OK) {
                console.log("Successfull post");
                document.getElementById("update-location-id").value = "";
                document.getElementById("update-location-name").value = "";
                document.getElementById("update-location-coords").value = "";
                var start = document.getElementById("start-span").innerHTML;
                fillTable(start);
                setState("default");
                document.getElementById("message-div").innerHTML = "Location has modified";
                document.getElementById("message-div").setAttribute("class", "alert alert-success");
            } else if (xhr.status == 400){
                var response = JSON.parse(xhr.responseText);
                document.getElementById("message-div").innerHTML = response.message;
                document.getElementById("message-div").setAttribute("class", "alert alert-danger");
            } else {
                console.log('Error: ' + xhr.responseText);
            }
          }
        };
        xhr.send(JSON.stringify(req));
        return false;
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