class Company {
    private employees: Employee[] = [];

    add(employee: Employee) {
        this.employees.push(employee);
    }

    getProjectList() {
        const projects = new Set<string>();
        for (const employee of this.employees) {
            projects.add(employee.getCurrentProject);
        }
        return Array.from(projects);
    }

    getNameList() {
        const names = new Set<string>();
        for (const employee of this.employees) {
            names.add(employee.getName);
        }
        return Array.from(names);
    }
}

class Employee {
    private name: string;
    private currentProject: string;

    constructor(name: string, currentProject: string) {
        this.name = name;
        this.currentProject = currentProject;
    }

    get getName() {
        return this.name;
    }

    set setName(newName: string) {
        this.name = newName;
    }

    set setProjectName(newProjectName: string) {
        this.currentProject = newProjectName;
    }

    get getCurrentProject() {
        return this.currentProject;
    }
}

class Backend extends Employee {}
class Frontend extends Employee {}

const b1 = new Backend("Name1", "Project1");
const b2 = new Backend("Name2", "Project2");
const b3 = new Backend("Name3", "Project3");
const f1 = new Frontend("Name4", "Project1");
const f2 = new Frontend("Name5", "Project2");
const f3 = new Frontend("Name6", "Project3");

const company = new Company();
company.add(b1);
company.add(b2);
company.add(b3);
company.add(f1);
company.add(f2);
company.add(f3);

const names = company.getNameList();
console.log(names);
const projects = company.getProjectList();
console.log(projects);
const body = document.body;
const container = document.createElement("div");
const div1 = document.createElement("div");
const div2 = document.createElement("div");
container.style.display = "flex";
container.style.flexDirection = "column";
div1.style.display = "flex";
div2.style.display = "flex";
div1.style.flexDirection = "column";
div2.style.flexDirection = "column";

for (let i = 0; i < names.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = names[i];
    div1.appendChild(p);
}

for (let i = 0; i < projects.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = projects[i];
    div2.appendChild(p);
}

container.appendChild(div1);
container.appendChild(div2);
body.appendChild(container);

