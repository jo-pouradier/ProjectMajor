// const URL_PREFIX = "http://127.0.0.1:80"
const URL_PREFIX = ""
const FIRE_URL = "/fire"
const VEHICLE_URL = "/vehicle"
const TEAMS_URL = "/team"
const FACILITY_URL = "/facility"

const getUserToken = () => {
    //TODO just return the team tocken
    return localStorage.getItem("token");
}
const getAllFire = (callback, err_callback) => {
    let context =
        {method: 'GET'};
    fetch(URL_PREFIX + FIRE_URL + '/', context)
        .then(response => response.json())
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const getAllVehicle = (callback, err_callback) => {
    let context =
        {method: 'GET'};
    fetch(URL_PREFIX + VEHICLE_URL + "/getAllVehicle", context)
        .then(response => response.json())
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const delVehicle = (teamCode, obj_id, callback, err_callback) => {
    let context =
        {method: 'DELETE'};
    fetch(URL_PREFIX + VEHICLE_URL + '/' + "delVehicle" + '/' + teamCode + '/' + obj_id, context)
        .then(response => response.json())
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const addVehicle = (teamCode, obj, callback, err_callback) => {
    let context;
    const token = getUserToken();
    if (token !== undefined) {
        context =
            {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            };
    } else {
        context =
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            };
    }

    fetch(URL_PREFIX + VEHICLE_URL + '/addVehicle' + '/' + teamCode, context)
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const teamVehicle = (callback, err_callback) => {
    let context =
        {method: 'GET'};
    fetch(URL_PREFIX + VEHICLE_URL + "/getTeamVehicle", context)
        .then(response => response.json())
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const addFacility = (obj, callback, err_callback) => {
    const token = getUserToken();
    let context = {};
    if (token !== undefined) {
        context =
            {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            };
    } else {
        context =
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            };
    }

    fetch(URL_PREFIX + FACILITY_URL, context)
        .then(response => callback(response))
        .catch(error => err_callback(error));
}

const delFacility = (obj_id, callback, err_callback) => {
    const token = getUserToken();
    let context = {};
    if (token !== undefined) {
        context =
            {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                }
            };
    } else {
        context =
            {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                }
            };
    }

    fetch(URL_PREFIX + FACILITY_URL + '/' + obj_id, context)
        .then(response => callback(response))
        .catch(error => err_callback(error));
}


const getAllFacilities = (callback, err_callback) => {
    let context =
        {method: 'GET'};
    fetch(URL_PREFIX + FACILITY_URL + '/', context)
        .then(response => response.json())
        .then(response => callback(response))
        .catch(error => err_callback(error));
}



export {
    getAllFire,
    getAllVehicle,
    delVehicle,
    addVehicle,
    teamVehicle,
    addFacility,
    delFacility,
    getAllFacilities
}