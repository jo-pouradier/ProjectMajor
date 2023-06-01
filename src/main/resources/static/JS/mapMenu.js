import {getAllVehicle, teamVehicle} from "./utils/httpApi.js";
import {displayTrucks, displayFires, displayLimitSquare, removeLimitSquare} from "./utils/displayMap.js";


let isLimitDisplay = false;
let isTeamTruckDisplay = false;
let isTeamFacilityDisplay = false;

async function displayLimitSquareF() {
    isLimitDisplay = !isLimitDisplay;
    if (isLimitDisplay) {
        displayLimitSquare();
    } else {
        removeLimitSquare();
    }
}

async function displayTeamTrucksF() {
    if (!isTeamTruckDisplay) {
        teamVehicle(displayTrucks, (err) => console.log(err));
        isTeamTruckDisplay = true;
    } else {
        getAllVehicle(displayTrucks, (err) => console.log(err));
        isTeamTruckDisplay = false;
    }
}

/**
 * @description Display the menu on the map and replace it when closed.
 */
function toggleMenuF() {
    let menu = document.getElementById('menu');
    if (menu.style.display === 'none') {
        menu.style.display = 'contents';
    } else {
        menu.style.display = 'none';
        document.getElementById('menuContainer').style.top = 81 + 'px';
        document.getElementById('menuContainer').style.left = 0 + 'px';
    }
}

function displayTeamFacilityF() {
    // TODO: display team facility (how to get team facility?)
    if (!isTeamFacilityDisplay) {

        isTeamFacilityDisplay = true;
    } else {
        isTeamFacilityDisplay = false;
    }
}

window.toggleMenu = function toggleMenu() {
    toggleMenuF()
}
window.displayLimitSquare = function displayLimitSquare() {
    displayLimitSquareF()
}
window.displayTeamTrucks = function displayTeamTrucks() {
    displayTeamTrucksF()
}
