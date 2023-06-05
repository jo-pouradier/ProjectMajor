import {COLORS_FIRES, FIRE_TYPE} from "./EnumValues.js";
import {getAllFacilities, getAllFire} from "./httpApi.js";

/**
    * @description Create a map with Jawg.io tiles.
 */
const map = L.map('map').setView([45.75, 4.85], 13);
const jawgToken = "R6cQuklra3e9KwQdlLMguhgLQVrbAXRctK2fpXtSJpvI7VUWPdyZH0r0IrnRSlV9";

L.tileLayer(
    `https://tile.jawg.io/jawg-light/{z}/{x}/{y}.png?access-token=${jawgToken}`, {
        attribution: '<a href="http://jawg.io" title="Tiles Courtesy of Jawg Maps" target="_blank" class="jawg-attrib">&copy; <b>Jawg</b>Maps</a> | <a href="https://www.openstreetmap.org/copyright" title="OpenStreetMap is open data licensed under ODbL" target="_blank" class="osm-attrib">&copy; OSM contributors</a>',
    }
).addTo(map);


/**
 * @description create of team grouplayer and everybody group layer
 * @description manage layer control
 */

let everybodyTruckGroupLayer = L.layerGroup().addTo(map);
let teamTruckGroupLayer = L.layerGroup().addTo(map);
let facilityTeamGroupLayer = L.layerGroup().addTo(map);
let facilityEverybodyGroupLayer = L.layerGroup().addTo(map);
let areaOfWork = L.layerGroup().addTo(map);
let fireGroupLayer = L.layerGroup().addTo(map);
var overlayMaps = {
    "Area of work": areaOfWork,
    "Everybody": everybodyTruckGroupLayer,
    "Team": teamTruckGroupLayer,
    "Facility Team": facilityTeamGroupLayer,
    "Facility Everybody": facilityEverybodyGroupLayer,
    "Fire": fireGroupLayer
};
var baseMaps = {
}
var layerControl = L.control.layers(baseMaps,overlayMaps).addTo(map);


/**
 *@description Create a polygon to show the game limit.
 */

const polygon = L.polygon([
    [45.6735, 4.9912],
    [45.6735, 4.7637],
    [45.826, 4.7637],
    [45.826, 4.9912]

])
polygon.addTo(map);
polygon.addTo(areaOfWork);


/**
 * @description Create a mapFacilityRefIDColor to show the facility color.
 */

let mapFacilityRefIDColor = new Map();
let mapTruckIcon = new Map();
let mapFireTypeColor =new Map();
FIRE_TYPE.reduce((map, fireType, index) => {
    map[fireType] = COLORS_FIRES[index];
    return map;
}, {});

getAllFacilities(setupColorByFacilityRefID, (err) => console.log(err));

/**
    * @description Create a mapTruckRefIDColor to show the truck color. Blue for everybody and red for team.
 *
 */
const teamFacilityId = [3796,3927];
function setupColorByFacilityRefID(facilities) {
    facilities.forEach((facility, index) => {
        if (teamFacilityId.includes(facility.id)){
            mapFacilityRefIDColor.set(facility.id, "red");
        } else{
            mapFacilityRefIDColor.set(facility.id, "blue");
        }
    });
    return mapFacilityRefIDColor;}

/**
 * @description setup the color of the fire
 */
setupColorByFireType();
function setupColorByFireType() {
    FIRE_TYPE.forEach((type, index) => {
        mapFireTypeColor.set(type, COLORS_FIRES[index]);
    });
    return mapFireTypeColor;
}
/**
 * @description Create a marker icon from a svg string.
 * @param color
 * @param svgString
 * @returns {*}
 */


function createMarkerIconFromSvgString(color, svgString) {
    return L.divIcon({
        className: color + 'Truck',
        html: svgString,
        fill: color,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [2, 2],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
}

/**
 * @description Create a marker icon from a svg string. with fetch to get the svg string
 * @param url
 * @returns {Promise<unknown>}
 */
function getImageString(url) {
    return fetch(url)
        .then((response) => {
            if (response.ok) {
                return response.blob();
            } else {
                throw new Error('Network response was not ok.');
            }
        })
        .then((blob) => {
            return new Promise((resolve) => {
                const reader = new FileReader();

                reader.onloadend = () => {
                    resolve(reader.result);
                };

                reader.readAsDataURL(blob);
            });
        })
        .catch((err) => {
            console.error('Error fetching image:', err);
            return undefined;
        });
}

/**
 * @description display facility on the map
 * @type {*[]}
 * @param listFacilityIcon : list of facility icon
 */

let listFacilityIcon = [];

async function displayFacility(facilityJson) {

    const facilitySvgStringBase64 = await getImageString('/image/svg/facility.svg');
    const facilitySvg = atob(facilitySvgStringBase64.split(',')[1]);

    facilityJson.forEach(facility => {
        let facilityIcon = createMarkerIconFromSvgString(mapFacilityRefIDColor.get(facility.id), facilitySvg); //create icon and add color
        facilityIcon.options.className = mapFacilityRefIDColor.get(facility.id) + "Facility facility"; //add class to icon

        const marker = L.marker([facility.lat, facility.lon], {icon: facilityIcon}).addTo(map); //create marker and add icon to marker
        let desc = "";
        for (const [key, value] of Object.entries(facility)) {
            desc += `<b>${key}: ${value}</b><br>`;
        } //create description for popup with all the facility information
        marker.bindPopup(desc); //add popup to marker
        listFacilityIcon.push(marker);

        if (teamFacilityId.includes(facility.id)) {
            marker.addTo(facilityTeamGroupLayer); //add marker to facilityTeamGroupLayer
        } else {
            marker.addTo(facilityEverybodyGroupLayer); //add marker to facilityEverybodyGroupLayer
        }
    });
}

/**
 * @description display for first time truck on the map
 * @type {*[]}
 */
async function displayTrucks(truckJson) {
    const truckSvgStringBase64 = await getImageString('/image/svg/truck.svg');
    const truckSvg = atob(truckSvgStringBase64.split(',')[1]);

    truckJson.forEach(truck => {
        let truckIcon = createMarkerIconFromSvgString(mapFacilityRefIDColor.get(truck.facilityRefID), truckSvg); //create icon and add color
        truckIcon.options.className = mapFacilityRefIDColor.get(truck.facilityRefID) + "Facility facility"; //add class to icon

        const marker = L.marker([truck.lat, truck.lon], {icon: truckIcon}).addTo(map); //create marker and add icon to marker
        let desc = "";
        for (const [key, value] of Object.entries(truck)) {
            desc += `<b>${key}: ${value}</b><br>`;
        } //create description for popup with all the truck information
        marker.bindPopup(desc); //add popup to marker
        mapTruckIcon.set(truck.id, marker);
        if (teamFacilityId.includes(truck.facilityRefID)) {
            marker.addTo(teamTruckGroupLayer); //add marker to teamTruckGroupLayer
        } else {
            marker.addTo(everybodyTruckGroupLayer); //add marker to everybodyTruckGroupLayer
        }
    });
}

/**
 * @description update truck position on the map
 * @param trucksjson
 * @returns {Promise<void>}
 */

async function updateTruckPos(trucksjson) {
        trucksjson.forEach(truckjson => {
            let truck = mapTruckIcon.get(truckjson.id);
            truck.setLatLng([truckjson.lat, truckjson.lon]);
        });
}

/**
 * @description display fire position from the server
 */

function createMarkerIcon(iconUrl) {
    return new L.Icon({
        iconUrl: iconUrl,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [20, 20],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [20, 20]
    });
}


async function displayFirePos(fireJson) {
    fireJson.forEach(fire => {
        // console.log(fire);
        let color = mapFireTypeColor.get(fire.type) || "blue";
        const fireIcon = createMarkerIcon( `../images/${color}Fire.png`);
        fireIcon.options.className = "fire"; //add class to icon

        const marker = L.marker([fire.lat, fire.lon], {icon: fireIcon}).addTo(map); //create marker and add icon to marker
        let desc = "";
        for (const [key, value] of Object.entries(fire)) {
            desc += `<b>${key}: ${value}</b><br>`;
        } //create description for popup with all the fire information
        marker.bindPopup(desc); //add popup to marker
        marker.addTo(fireGroupLayer); //add marker to fireGroupLayer
    });
}
export {
    displayFacility,
    displayTrucks,
    updateTruckPos,
    displayFirePos
}