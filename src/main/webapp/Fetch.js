/* 
 * JPA, REST, Test, CI and Travis with our start code
 * part 2
 */

/**
 * To event listeners til de to knapper
 */
document.querySelector("#getUser").onclick = getUser;
document.querySelector("#getAllUsers").onclick = getAllUsers;

/**
 * Udskriver enkelt film.
 */
function getUser(e) {
    e.preventDefault();
    var ID = document.querySelector("#userID").value;
    let url = "/review_week36_monday/api/movies/" + ID;
    console.log(url);
    fetch(url)
            .then(res => res.json()) //in flow1, just do it
            .then(data => {
                // Inside this callback, and only here, the response data is available
//                console.log("data", data);
                var tags = [];
                document.querySelector("#users").innerHTML = data.comp;
                Object.keys(data).forEach(function (key) {
                    console.log(key + " : " + data[key]);
                    tags.push(key + " : " + data[key]);
                });
                document.querySelector("#users").innerHTML = tags.join("<br />");
            });
}

/**
 * Laver tabel over alle film
 */
function getAllUsers(e) {
    e.preventDefault();
    let url = "/review_week36_monday/api/movies/all"; 
    console.log(url);
    fetch(url)
            .then(res => res.json()) //in flow1, just do it
            .then(data => {
                // Inside this callback, and only here, the response data is available
                var table = [];
                var name;
                var phone;
                console.log("data.list: "+ data.list);
                table.push("<thead><tr><th>Title:</th><th>Year:</th></th></tr></thead>");
                document.querySelector("#users").innerHTML = data.comp;
                data.list.forEach(function (k) {
                    Object.keys(k).forEach(function (key) {
                        if (key === 'title') {
                            title = k[key];
                            console.log("title: " + title);
                        }
                        if (key === 'year') {
                            year = k[key];
                            console.log("year: " + year);
                        }
                    });
                    table.push("<tbody><tr><td>" + title + "</td><td>" + year 
                            + "</td></tr></tbody>");
                });
                document.querySelector("#users").innerHTML =
                        "<table id=\"carTable\" class=\"table\">"
                        + table.join("") + "</table>";
            });
}




