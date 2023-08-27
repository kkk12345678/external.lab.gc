const divItems = document.getElementById("items");
const loader = document.getElementById("loader");
const searchInput = document.getElementById("search-input");
const select = document.getElementById("select-categories");
const categories = JSON.parse(localStorage.getItem("categories"));
const divCategories = document.getElementById("div-categories");

let offset = 0;

window.addEventListener('scroll',() => {
    if (window.scrollY + window.innerHeight >
        document.documentElement.scrollHeight) {
        renderDiv();
    }
})

let typingTimer;

searchInput.onkeyup = () => {
    clearTimeout(typingTimer);
    typingTimer = setTimeout(() => {
        divItems.innerHTML = "";
        items = loadItems(select.value, searchInput.value);
        offset = 0;
        renderDiv();
    }, 2000);
}

searchInput.onkeydown = () => {
    clearTimeout(typingTimer);
}

select.onchange = () => {
    divItems.innerHTML = "";
    items = loadItems(select.value, searchInput.value);
    offset = 0;
    renderDiv();
}

const equalsIgnoreCase = (str1, str2) => {
    if (str1.length !== str2.length) {
        return false;
    }
    for (let i = 0; i < str2.length; i++) {
        if (str1.charAt(i).toLowerCase() !== str2.charAt(i).toLowerCase()) {
            return false;
        }
    }
    return true;
}

const loadItems = (categoryId, search = "") => {
    const allItems =  JSON.parse(localStorage.getItem("items"));
    const items = [];
    for (let i = 0; i < allItems.length; i++) {
        if (categoryId === "0" && search === "") {
            items.push(allItems[i]);
        } else if (categoryId !== "0" && search === "") {
            if (categoryId === allItems[i].categoryId) {
                items.push(allItems[i]);
            }
        } else if (categoryId === "0" && search !== "") {
            if (equalsIgnoreCase(search, allItems[i].name.substring(0, search.length))) {
                items.push(allItems[i]);
            }
        } else {
            if (equalsIgnoreCase(search, allItems[i].name.substring(0, search.length))
                && categoryId === allItems[i].categoryId) {
                items.push(allItems[i]);
            }
        }
    }
    return items;
}

let items = loadItems(select.value);

const sleep = (milliseconds) => {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
}

const numItems = 8;

const renderDiv = () => {
    loader.style.display = "block";
    sleep(1000);
    for (let i = offset; i < Math.min(offset + numItems, items.length); i++) {
        const name = items[i].name;
        const item = document.createElement("div");
        item.className = "item";
        const a = document.createElement("a");
        a.href = "item.html?id=" + items[i].id;
        const img = document.createElement("img");
        img.src = "../img/item.jpg";
        img.alt = name;
        img.style.width = "250px";
        const imageBox = document.createElement("div");
        imageBox.className = "image-box";
        imageBox.appendChild(img);
        a.appendChild(imageBox);
        const details = document.createElement("div");
        details.className = "details";
        const name_ = document.createElement("div");
        name_.className = "name";
        const description = document.createElement("div");
        description.className = "description"
        const cart = document.createElement("div");
        cart.className = "add-to-cart";
        cart.innerHTML = "<p>$" + items[i].price + "</p><button class=\"add-to-cart-button\">Add to cart</button>";
        let descriptionText = items[i].description;
        if (descriptionText.length > 50) {
            descriptionText = descriptionText.substring(0, 50) + "...";
        }
        name_.innerHTML = "<p class=\"item-name\">" + items[i].name + "</p><a href=\"#\"><span class=\"material-icons\">favorite</span></a>"
        description.innerHTML = "<p class=\"brief-description\">" + descriptionText + "</p><p class=\"expiration\">Expires in 3 days</p>";
        details.appendChild(name_);
        details.appendChild(description);
        details.appendChild(cart);
        item.appendChild(a);
        item.appendChild(details);
        divItems.appendChild(item);
        divItems.appendChild(item);
    }
    offset += numItems;
    if (offset >= items.length) {
        loader.style.display = "none";
    }
}


for (let i = 0; i < categories.length; i++) {
    const categoryName =categories[i].name;
    const option = document.createElement("option");
    option.value = categories[i].id;
    option.innerHTML = categoryName;
    option.className = "option";
    select.appendChild(option);
    const container = document.createElement("div");
    const middle = document.createElement("div");
    const text = document.createElement("div");
    const img = document.createElement("img");
    text.innerHTML = categoryName;
    text.className = "text";
    middle.className = "middle";
    container.className = "container";
    img.src = "../img/img.png";
    img.width = 150;
    img.alt = categoryName;
    img.className = "category-image";
    middle.appendChild(text);
    container.appendChild(img);
    container.appendChild(middle);
    divCategories.appendChild(container);
}

renderDiv();