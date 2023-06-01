import {getAllFacilities, getAllFire, getAllVehicle} from "./utils/httpApi.js";
import {displayFacility, displayFires, displayTrucks} from "./utils/displayMap.js";


getAllVehicle(displayTrucks, (err) => console.log(err));
getAllFire(displayFires, (err) => console.log(err));
getAllFacilities(displayFacility, (err) => console.log(err));
