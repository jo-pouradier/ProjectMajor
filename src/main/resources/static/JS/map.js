const map = L.map('map').setView([45.75, 4.85], 13);
// polygon representing the limit of the game
const polygon = L.polygon([
    [45.65, 4.75],
    [45.65, 4.95],
    [45.85, 4.95],
    [45.85, 4.75]
])
let listTrucksIcon = [];
let listFiresIcon = [];
const token = "R6cQuklra3e9KwQdlLMguhgLQVrbAXRctK2fpXtSJpvI7VUWPdyZH0r0IrnRSlV9";
let color = "blue";
const listColorTrucks = ["red", "blue", "green", "gold", "orange", "violet", "black", "grey", "yellow"];
const listColorFires = ['yellow', 'blue', 'purple', 'pink', 'green', 'red'];
const mapFireTypeColor = {
    'E_Electric': 'blue',
    'B_Gasoline': 'green',
    'B_Plastics': 'purple',
    'C_Flammable_Gases': 'pink',
    'D_Metals': 'yellow',
    'B_Alcohol': 'red'
};

let isLimitDisplay = false;
let isTeamTruckDisplay = false;

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

L.tileLayer(
    `https://tile.jawg.io/jawg-light/{z}/{x}/{y}.png?access-token=${token}`, {
        attribution: '<a href="http://jawg.io" title="Tiles Courtesy of Jawg Maps" target="_blank" class="jawg-attrib">&copy; <b>Jawg</b>Maps</a> | <a href="https://www.openstreetmap.org/copyright" title="OpenStreetMap is open data licensed under ODbL" target="_blank" class="osm-attrib">&copy; OSM contributors</a>',
    }
).addTo(map);


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
 * @description Create a marker icon from a svg string.
 * @param color the color of the icon
 * @param svgString the svg file as string
 * @returns {L.DivIcon}
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
    listTrucksIcon.forEach(truck => {
        map.removeLayer(truck);
    });
    listTrucksIcon = [];
    // set up a color for each facilityRefID
    const listFacilityRefID = trucksJson.map(truck => truck.facilityRefID);
    const uniqueFacilityRefID = [...new Set(listFacilityRefID)];
    const mapFacilityRefIDColor = new Map();
    uniqueFacilityRefID.forEach((facilityRefID, index) => {
        mapFacilityRefIDColor.set(facilityRefID, listColorTrucks[index]);
    });

    // fetch truck icon as svg string to create a marker icon
    const truckSvgStringBase64 = await getImageString('/image/svg/truck.svg');
    const truckSvg = atob(truckSvgStringBase64.split(',')[1]);
    const truckIcon = createMarkerIconFromSvgString("blue", truckSvg);

    // display each trucks on the map
    trucksJson.forEach(truck => {
        color = mapFacilityRefIDColor.get(truck.facilityRefID) || "blue";
        truckIcon.options.className = color + "Truck";
        const marker = L.marker([truck.lat, truck.lon], {icon: truckIcon,}).addTo(map);
        listTrucksIcon.push(marker);
        let desc = "";
        for (const [key, value] of Object.entries(truck)) {
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

// TODO tester cette focntion avec les vraies données
/**
 * @description Display the bounding boxe on the map.
 * @returns {Promise<void>}
 */
async function displayLimitSquare() {
    isLimitDisplay = !isLimitDisplay;
    if (isLimitDisplay) {
        polygon.addTo(map);
    } else {
        map.removeLayer(polygon);
    }

}


async function displayAllTrucks() {
    const trucksResponse = await fetch("/vehicle/getAllVehicle");
    const trucksJson = await trucksResponse.json();
    displayTrucks(trucksJson);
}

async function displayAllFires() {
    // get all fires from backend as dtos
    const firesResponse = await fetch("/fire/");
    const firesJson = await firesResponse.json();
    // display fires on the map
    displayFires(firesJson);
}

async function displayTeamTrucks() {
    if (!isTeamTruckDisplay) {
        const teamTrucksResponse = await fetch("/vehicle/getTeamVehicle");
        const teamTrucksJson = await teamTrucksResponse.json();
        await displayTrucks(teamTrucksJson);
        isTeamTruckDisplay = true;
    } else {
        await displayAllTrucks();
        isTeamTruckDisplay = false;
    }
}
// Display all trucks and fires
displayAllTrucks();
displayAllFires();

/**
 * @description Display the menu on the map and replace it when closed.
 */
function toggleMenu() {
    let menu = document.getElementById('menu');
    if (menu.style.display === 'none') {
        menu.style.display = 'contents';
    } else {
        menu.style.display = 'none';
        document.getElementById('menuContainer').style.top = 81 + 'px';
        document.getElementById('menuContainer').style.left = 0 + 'px';
    }
}

