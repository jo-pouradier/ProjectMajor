const map = L.map('map').setView([45.75, 4.85], 13);
const tocken = "R6cQuklra3e9KwQdlLMguhgLQVrbAXRctK2fpXtSJpvI7VUWPdyZH0r0IrnRSlV9"
let color = "blue";
const listColor = ["red", "blue", "green", "gold", "orange", "violet", "black", "grey", "yellow"];
const markerIcon = new L.Icon({
    iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`,
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

L.tileLayer(
    `https://tile.jawg.io/jawg-light/{z}/{x}/{y}.png?access-token=${tocken}`, {
        attribution: '<a href="http://jawg.io" title="Tiles Courtesy of Jawg Maps" target="_blank" class="jawg-attrib">&copy; <b>Jawg</b>Maps</a> | <a href="https://www.openstreetmap.org/copyright" title="OpenStreetMap is open data licensed under ODbL" target="_blank" class="osm-attrib">&copy; OSM contributors</a>',
    }
).addTo(map);

/**
 *@description display all trucks on the map
 * @returns {Promise<void>}
 */
async function displayTrucks() {
    const trucks = (await fetch("/vehicle/getAllVehicle"));
    const trucksJson = await trucks.json();
    console.log(trucksJson);
    // list of color for each different facilityRefID

    const listFacilityRefID = trucksJson.map(truck => truck.facilityRefID);
    // remove duplicate
    const uniqueFacilityRefID = [...new Set(listFacilityRefID)];
    // create a map with facilityRefID as key and color as value
    const mapFacilityRefIDColor = new Map();
    uniqueFacilityRefID.forEach((facilityRefID, index) => {
        mapFacilityRefIDColor.set(facilityRefID, listColor[index]);
    });
    console.log(mapFacilityRefIDColor)

    trucksJson.forEach(truck => {
        // set up the marker
        color = mapFacilityRefIDColor.get(truck.facilityRefID);
        if (color === undefined) color = "blue";
        markerIcon.options.iconUrl = `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`;
        const marker = L.marker([truck.lat, truck.lon], {icon: markerIcon}).addTo(map);
        // set up the popup
        let desc = ``;
        for (const [key, value] of Object.entries(truck)) {
            desc += `<b>${key}: ${value}</b><br>`;
        }
        marker.bindPopup(desc);
    });

}

// display all trucks
displayTrucks();
