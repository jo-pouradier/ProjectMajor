import {getAllFacilities, getAllFire, getAllVehicle} from "./utils/httpApi.js";
import {displayFacility, displayFires, displayTrucks} from "./utils/displayMap.js";

let isAppPause = false;
const fireTimeUpdate = 5000;
const vehicleTimeUpdate = 1000;
const facilityTimeUpdate = 10000;

getAllVehicle(displayTrucks, (err) => console.log(err));
getAllFire(displayFires, (err) => console.log(err));
getAllFacilities(displayFacility, (err) => console.log(err));

if (!isAppPause) {
    setInterval((isCallApiPause) => {
        if (!isCallApiPause) {
            getAllFire(displayFires, (err) => {
                console.error(err)
            });
        }
    }, fireTimeUpdate);

    setInterval((isCallApiPause) => {
        if (!isCallApiPause) {
            getAllVehicle(displayTrucks, (err) => {
                console.error(err)
            });
        }
    }, vehicleTimeUpdate);

    setInterval((isCallApiPause) => {
        if (!isCallApiPause) {
            getAllFacilities(displayFacility, (err) => {
                console.error(err)
            });
        }
    }, facilityTimeUpdate);
}

