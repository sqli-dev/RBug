const setup_java_bridge = () => {
    return {
        pushMethod: (state, name) => {
            if (regex != null && !regex.test(name)) return;
            let method = document.createElement("div");
            let stateSpan = document.createElement("span");
            method.className = "method";
            stateSpan.className = state == 0 ? "incoming" : "outcoming";
            method.appendChild(stateSpan);
            method.appendChild(createMethodDiv(name))
            document.getElementById("calls").appendChild(method);
        },
        pauseState: (bool) => {
            document.getElementById("state").innerText = "Paused: " + bool;
        },
        addSchedule: (name, state, arg, casts, uuid) => {
            const method = createMethodDiv(name,true,uuid);
            const argsDiv = document.createElement("div");
            argsDiv.className = "args";
            if (arg != null) {
                argsDiv.innerText = "Arg:"
                const argDiv = document.createElement("div");
                argDiv.className = "arg";
                argDiv.innerText = `(${casts}) `+arg;
                argsDiv.appendChild(argDiv);
            } else {
                argsDiv.innerText = "No arg."
            }
            method.appendChild(argsDiv);
            // <div className="methodNaming">
            //     asdasdasd
            //     <div className="args">
            //         Args:
            //         <div className="arg">
            //             Elo
            //         </div>
            //         <div className="arg">
            //             Elo
            //         </div>
            //     </div>
            // </div>
            document.getElementById("catched").appendChild(method);
            // document.getElementById("state").innerText = "Paused: " + bool;
        }
    }
}
let regex;
const typed = (inputField) => {
    if (inputField.value == null || inputField.value.length == 0) {
        regex = null;
    } else {
        regex = new RegExp(inputField.value);
    }
}
onload = () => {
    setInterval(() => {
        document.getElementById("response").innerText = "Response: " + Math.random().toFixed(3)
    }, 5);
}
const span = (input, color) => {
    const sp = document.createElement("span");
    sp.innerText = input + " ";
    sp.style.color = "#" + color;
    return sp;
}

const typeSyntax = ["public", "private", "protected", "static", "final", "volatile", "abstract", "default", "synchronized", "record", "interface", "enum", "class"];
const boldTypeSyntax = ["native", "strictfp", "transient"];

const colors = ["c86a2a", "f83a1a"];
const excludeTypes = (text) => {
    for (let type of typeSyntax) {
        if (text.indexOf(type) != -1) {
            text = text.replaceAll(type, "")
        }
    }
    for (let type of boldTypeSyntax) {
        if (text.indexOf(type) != -1) {
            text = text.replaceAll(type, "")
        }
    }
    return text;
}
const createTypes = (text) => {
    let spans = [];
    for (let type of typeSyntax) {
        if (text.indexOf(type) != -1) {
            spans.push(span(type, colors[0]))
            text = text.replaceAll(type, "")
        }
    }
    for (let type of boldTypeSyntax) {
        if (text.indexOf(type) != -1) {
            spans.push(span(type, colors[1]))
            text = text.replaceAll(type, "")
        }
    }
    return spans;
}
const schedulePause = (method) => {
    rdebugger.schedule(method);
}
const resume = (uuid) => {
    rdebugger.resume(uuid);
}
const createMethodDiv = (input, unpause=false, uuid="") => {
    const style = document.createElement("div");
    style.className = "methodNaming"
    const startIndex = input.indexOf("(");
    const endIndex = input.indexOf(")");

    // let args = "ERROR";

    if (startIndex !== -1 && endIndex !== -1 && endIndex > startIndex) {
        //   const extractedText = input.substring(startIndex + 1, endIndex);
        const beforeText = input.substring(0, startIndex);
        for (let type of createTypes(beforeText)) {
            style.appendChild(type);
        }
        const left = document.createElement("span");
        left.innerText = excludeTypes(beforeText);
        style.appendChild(left)
        style.setAttribute("onclick", (unpause?"resume":"schedulePause")+"('" + (unpause?uuid:input) + "');")
    }
    return style;
}