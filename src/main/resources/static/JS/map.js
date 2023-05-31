const map = L.map('map').setView([45.75, 4.85], 13);
const polygon = L.polygon([
    [45.65, 4.75],
    [45.65, 4.95],
    [45.85, 4.95],
    [45.85, 4.75]
])
let listTrucks = [];
let listFires = [];
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

const fireIcon = createMarkerIcon("blue", "../images/blueFire.png");

let isLimitDisplay = false;

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
 * @returns {Promise<void>}
 */
async function displayTrucks() {
    const trucksResponse = await fetch("/vehicle/getAllVehicle");
    const trucksJson = await trucksResponse.json();
    listTrucks = trucksJson;
    const listFacilityRefID = trucksJson.map(truck => truck.facilityRefID);
    const uniqueFacilityRefID = [...new Set(listFacilityRefID)];
    const mapFacilityRefIDColor = new Map();

    // fetch truck icon as svg string
    const truckSvgStringBase64 = await getImageString('/image/svg/truck.svg');
    const truckSvg = atob(truckSvgStringBase64.split(',')[1]);
    const truckIcon = createMarkerIconFromSvgString("blue", truckSvg);

    uniqueFacilityRefID.forEach((facilityRefID, index) => {
        mapFacilityRefIDColor.set(facilityRefID, listColorTrucks[index]);
    });
    console.log(trucksJson)

    trucksJson.forEach(truck => {
        color = mapFacilityRefIDColor.get(truck.facilityRefID) || "blue";
        truckIcon.options.iconUrl = `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`;
        const marker = L.marker([truck.lat, truck.lon], {icon: truckIcon,}).addTo(map);
        let desc = "";
        for (const [key, value] of Object.entries(truck)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });
}

async function displayFires() {
    const firesResponse = await fetch("/fire/");
    const firesJson = await firesResponse.json();
    listFires = firesJson;
    firesJson.forEach(fire => {
        color = mapFireTypeColor.get(fire.fireType) || "blue";
        fireIcon.options.iconUrl = `../images/${color}Fire.png`;
        const marker = L.marker([fire.lat, fire.lon], {icon: fireIcon}).addTo(map);
        let desc = "";
        for (const [key, value] of Object.entries(fire)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });
}

// TODO tester cette focntion avec les vraies données
async function displayLimitSquare() {
    isLimitDisplay = !isLimitDisplay;
    if (isLimitDisplay) {
        polygon.addTo(map);
    } else {
        map.removeLayer(polygon);
    }

}

// Display all trucks and fires
displayTrucks();
displayFires();

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

