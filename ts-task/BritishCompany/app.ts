interface ILocation {
    addPerson(person: Employee): void;
    getPerson(index: number): Employee;
    getCount(): number;
}

class CompanyLocationArray implements ILocation {
    private employees: Employee[] = [];

    addPerson(person: Employee): void {
        this.employees.push(person);
    }

    getCount(): number {
        return this.employees.length;
    }

    getPerson(index: number): Employee {
        return this.employees[index];
    }
}

class CompanyLocationLocalStorage  implements ILocation {
    addPerson(person: Employee): void {
        const json = localStorage.getItem("employees");
        const employees = json !== null ? JSON.parse(json) : [];
        employees.push(person);
        localStorage.setItem("employees", JSON.stringify(employees));
    }

    getCount(): number {
        const json = localStorage.getItem("employees");
        return json !== null ? JSON.parse(json).length : 0;
    }

    getPerson(index: number): Employee {
        const json = localStorage.getItem("employees");
        return json !== null ? JSON.parse(json)[index] : null;
    }
}

class Company {
    private readonly employees: Employee[];
    private readonly location: ILocation;


    constructor(location: ILocation) {
        this.location = location;
        this.employees = [];
    }

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

export class Employee {
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


const b1 = new Employee("Name1", "Project1");
const b2 = new Employee("Name2", "Project2");
const b3 = new Employee("Name3", "Project3");
const f1 = new Employee("Name4", "Project1");
const f2 = new Employee("Name5", "Project2");
const f3 = new Employee("Name6", "Project3");

const location1 = new CompanyLocationArray();
const location2 = new CompanyLocationLocalStorage();

const company1 = new Company(location1);
const company2 = new Company(location2);


company1.add(b1);
company1.add(b2);
company2.add(b3);
company2.add(f1);
company2.add(f2);
company1.add(f3);

const names1 = company1.getNameList();
const names2 = company2.getNameList();
console.log(names1);
console.log(names2);
const projects1 = company1.getProjectList();
const projects2 = company2.getProjectList();
console.log(projects1);
console.log(projects2);

const body = document.body;
const container = document.createElement("div");
const div1 = document.createElement("div");
const div2 = document.createElement("div");
const div3 = document.createElement("div");
const div4 = document.createElement("div");
container.style.display = "flex";
container.style.flexDirection = "column";
div1.style.display = "flex";
div2.style.display = "flex";
div1.style.flexDirection = "column";
div2.style.flexDirection = "column";
div3.style.display = "flex";
div4.style.display = "flex";
div3.style.flexDirection = "column";
div4.style.flexDirection = "column";

for (let i = 0; i < names1.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = names1[i];
    div1.appendChild(p);
}

for (let i = 0; i < names2.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = names2[i];
    div3.appendChild(p);
}

for (let i = 0; i < projects1.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = projects1[i];
    div2.appendChild(p);
}

for (let i = 0; i < projects2.length; i++) {
    const p = document.createElement("p");
    p.innerHTML = projects2[i];
    div4.appendChild(p);
}


container.appendChild(div1);
container.appendChild(div2);
container.appendChild(div3);
container.appendChild(div4);
body.appendChild(container);
