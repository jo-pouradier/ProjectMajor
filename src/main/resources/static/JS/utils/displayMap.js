import {COLORS_FIRES, COLORS_TRUCKS, FIRE_TYPE} from "./EnumValues.js";
import {getAllFacilities} from "./httpApi.js";

let listFiresIcon = [];
let listFacilityIcon = [];
let mapFacilityRefIDColor = new Map();
let mapTruckJsonIcon = new Map();
// setup color by facility refID
getAllFacilities(setupColorByFacilityRefID, (err) => console.log(err));
let color = "blue";
const map = L.map('map').setView([45.75, 4.85], 13);
const jawgToken = "R6cQuklra3e9KwQdlLMguhgLQVrbAXRctK2fpXtSJpvI7VUWPdyZH0r0IrnRSlV9";


L.tileLayer(
    `https://tile.jawg.io/jawg-light/{z}/{x}/{y}.png?access-token=${jawgToken}`, {
        attribution: '<a href="http://jawg.io" title="Tiles Courtesy of Jawg Maps" target="_blank" class="jawg-attrib">&copy; <b>Jawg</b>Maps</a> | <a href="https://www.openstreetmap.org/copyright" title="OpenStreetMap is open data licensed under ODbL" target="_blank" class="osm-attrib">&copy; OSM contributors</a>',
    }
).addTo(map);

const polygon = L.polygon([
    [45.6735, 4.9912],
    [45.6735, 4.7637],
    [45.826, 4.7637],
    [45.826, 4.9912]

])

const displayLimitSquare = () => {
    polygon.addTo(map);
}

const removeLimitSquare = () => {
    map.removeLayer(polygon);
}

function setupColorByFacilityRefID(facilities) {
    facilities.forEach((facility, index) => {
        mapFacilityRefIDColor.set(facility.id, COLORS_TRUCKS[index]);
    });
    return mapFacilityRefIDColor;
}

const mapFireTypeColor = FIRE_TYPE.reduce((map, fireType, index) => {
    map[fireType] = COLORS_FIRES[index];
    return map;
}, {});

function createMarkerIcon(color, iconUrl) {
    return new L.Icon({
        iconUrl: iconUrl,
        fill: color,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
}

/**
 * @description Get the image from backend as a base64 string.
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
 * @description Create a marker icon from a svg string.
 * @param color the color of the icon
 * @param svgString the svg file as string
 * @returns L.divIcon the icon to display on the map
 * @todo make the type of the truck as a parameter, same goes for the class
 */
function createMarkerIconFromSvgString(color, svgString) {
    return L.divIcon({
        className: color + 'Truck',
        html: svgString,
        fill: color,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [35, 35],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
}

/**
 * @description Display all trucks on the map.
 * @param trucksJson list of trucks as json
 * @returns {Promise<void>}
 */
async function displayTrucks(trucksJson) {
    if (mapTruckJsonIcon.size !== 0) {
        // look for trucks that have moved
        // TODO this filter part doesn't work (trucks are blinking without moving)
        let currentTrucks = Array.from(mapTruckJsonIcon.entries());
        trucksJson.filter((truck, index, array) => {
            currentTrucks.forEach(currentTruckJsonIcon => {
                if (truck.facilityRefID === currentTruckJsonIcon[0].facilityRefID && (truck.lon !== currentTruckJsonIcon[0].lon || truck.lat !== currentTruckJsonIcon[0].lat)) {
                    // the truck has moved, we remove it from the map
                    mapTruckJsonIcon.delete(currentTruckJsonIcon[0]);
                    map.removeLayer(currentTruckJsonIcon[1]);
                    // return the truck for further processing
                    return truck;
                }
            })
        })
    }

    // fetch truck icon as svg string to create a marker icon
    const truckSvgStringBase64 = await getImageString('/image/svg/truck.svg');
    const truckSvg = atob(truckSvgStringBase64.split(',')[1]);
    const truckIcon = createMarkerIconFromSvgString("blue", truckSvg);

    // display each remaining trucks on the map
    trucksJson.forEach(truck => {
        color = mapFacilityRefIDColor.get(truck.facilityRefID) || "blue";
        truckIcon.options.className = color + "Truck";
        const marker = L.marker([truck.lat, truck.lon], {icon: truckIcon,}).addTo(map);
        mapTruckJsonIcon.set(truck, marker);
        let desc = "";
        for (const [key, value] of Object.entries(truck)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });
}

async function displayFacility(facilityJson) {
    listFacilityIcon.forEach(facility => {
        map.removeLayer(facility);
    });
    listFacilityIcon = [];

    const facilitySvgStringBase64 = await getImageString('/image/svg/facility.svg');
    const facilitySvg = atob(facilitySvgStringBase64.split(',')[1]);
    const facilityIcon = createMarkerIconFromSvgString("blue", facilitySvg);

    facilityJson.forEach(facility => {
        const color = mapFacilityRefIDColor.get(facility.id) || "blue";
        facilityIcon.options.className = color + "Facility facility";
        const marker = L.marker([facility.lat, facility.lon], {icon: facilityIcon}).addTo(map);
        listFacilityIcon.push(marker);
        let desc = "";
        for (const [key, value] of Object.entries(facility)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });
}

/**
 * @description Display all fires on the map.
 * @returns {Promise<void>}
 */
async function displayFires(firesJson) {
    listFiresIcon.forEach(fire => {
        map.removeLayer(fire);
    });
    listFiresIcon = [];

    firesJson.forEach(fire => {
        // set up a color for each fire type
        color = mapFireTypeColor.get(fire.fireType) || "blue";
        const fireIcon = createMarkerIcon("blue", `../images/${color}Fire.png`);
        const marker = L.marker([fire.lat, fire.lon], {icon: fireIcon}).addTo(map);
        listFiresIcon.push(marker);
        let desc = "";
        // set up the popup content with all fire properties
        for (const [key, value] of Object.entries(fire)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });
}

export {
    createMarkerIcon,
    createMarkerIconFromSvgString,
    displayTrucks,
    displayFires,
    getImageString,
    displayLimitSquare,
    removeLimitSquare,
    displayFacility
}

