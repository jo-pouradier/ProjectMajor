function aff_Camion(Camions) {

    let template = document.getElementById('Camions');



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
    const response = await fetch("JS/camion_test.json");
    const camions_json = await response.json();
    console.log(camions_json)
    aff_Camion(camions_json);
}

$(document).ready(function () {
    affichage_camions().then(r => console.log("camions affichés"));
})
