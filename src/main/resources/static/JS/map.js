import {getAllFire, getAllVehicle} from "./utils/httpApi.js";
import {displayFires, displayTrucks} from "./utils/displayMap.js";


getAllVehicle(displayTrucks, (err) => console.log(err));
getAllFire(displayFires, (err) => console.log(err));
