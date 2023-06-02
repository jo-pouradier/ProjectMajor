    async function aff_Camion(Camions) {

    let template = document.getElementById('Camions');


    console.log(Camions)
    for (const C of Camions) { // itère sur le tableau

        let clone = document.importNode(template.content, true);      // clone le template
        newContent2 = clone.firstElementChild.innerHTML // remplace {{modèle}}
            .replace(/{{id}}/g, C.id) // et {{couleur}} par
            .replace(/{{type}}/g, C.type)
            .replace(/{{lon}}/g, C.lon)
            .replace(/{{lat}}/g, C.lat)
            .replace(/{{liquideType}}/g, C.liquidType)
            .replace(/{{liquideQuantity}}/g, C.liquidQuantity)
            .replace(/{{fuel}}/g, C.fuel)
            .replace(/{{crewMember}}/g, C.crewMember)
            .replace(/{{facilityRefId}}/g, C.facilityRefID)

        clone.firstElementChild.innerHTML = newContent2
        template.parentNode.appendChild(clone)}
    console.log(template)
}

async function affichage_camions() {
    let toClear = document.getElementById('rendu');
    while(toClear.children.length != 1){
        toClear.children[1].remove();
    }
    let response = (await fetch("/vehicle/getTeamVehicle"));
    const camions_json = await response.json();
    console.log(camions_json)
    await aff_Camion(camions_json);
}


async function createCamion() {
    var type = document.getElementById('type').value;
    var liquidType = document.getElementById('liquidType').value;
    var FacilityRefID = document.getElementById('FacilityRefID').value;
    if(FacilityRefID === ""){FacilityRefID = 3796;}
    console.log("id facility : "+FacilityRefID)
    let response = (await fetch("/facility/"+FacilityRefID));
    const facility = await response.json();
    let response2 = (await fetch("/vehicle/getVehicleTypeInfo?param="+type, {method: 'GET'}));
    const vehicleTypeInfo = await response2.json();
    let header = {
        "crewMember": vehicleTypeInfo.crewCapacity,
        "facilityRefID": facility.id,
        "fuel": vehicleTypeInfo.fuelCapacity,
        "lat": facility.lat,
        "liquidQuantity": vehicleTypeInfo.liquidCapacity,
        "liquidType": liquidType,
        "lon": facility.lon,
        "type": type
    }
    let response3 = (await fetch("/vehicle/createVehicle", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(header)
    }));
    affichage_camions().then(r => console.log("camions affichés"));
    console.log("Camion créé")
}

async function deleteCamion(id) {
    let response = (await fetch("/vehicle/delVehicle/"+id, {method: 'DELETE'}));
    console.log(id)
    affichage_camions().then(r => console.log("camions affichés"));
}


$(document).ready(function () {
    affichage_camions().then(r => console.log("camions affichés"));
})
