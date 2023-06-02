import {getAllFacilities, getAllFire, getAllVehicle} from "./utils/httpApi.js";
import {displayFacility, displayFirePos, displayTrucks, updateTruckPos} from "./utils/displayMap.js";
//displayFires, displayTrucks
let isAppPause = false;
const fireTimeUpdate = 5000;
const vehicleTimeUpdate = 1000;
const facilityTimeUpdate = 10000;

getAllVehicle(displayTrucks, (err) => console.log(err));
//getAllFire(displayFires, (err) => console.log(err));
console.log("test");
getAllFacilities(displayFacility, (err) => console.log(err));

// if (!isAppPause) {
setInterval((isCallApiPause) => {
    if (!isCallApiPause) {
        getAllFire(displayFirePos, (err) => {
            console.error(err)
        });
    }
    }, fireTimeUpdate);
//
setInterval((isCallApiPause) => {
    if (!isCallApiPause) {
        getAllVehicle(updateTruckPos, (err) => {
            console.error(err)
        });
    }
}, vehicleTimeUpdate);
