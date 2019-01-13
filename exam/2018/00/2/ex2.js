
//first
if (typeof SVGSVGElement.pauseAnimations !== "undefined") {
}

if (typeof SVGSVGElement.pauseAnimations === "function"){
}

//second
try{
    SVGSVGElement.pauseAnimations()
}
catch (e) {
}

//third
if ("pauseAnimations" in SVGSVGElement){
}

//fourth
if (SVGSVGElement.hasOwnProperty("pauseAnimations")){ //annab false miskipärast
}
