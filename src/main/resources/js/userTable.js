
async function dataAboutAllUsers() {
    const response = await fetch("/api/users");
    return await response.json();
}

async function dataAboutCurrentUser() {
    const response = await fetch("/api/user")
    return await response.json();
}


async function fillTableOfAllUsers() {
    const usersTable = document.getElementById("usersTableId");
    const users = await dataAboutAllUsers();


    let usersTableHTML = "";
    for (let user of users) {
        usersTableHTML +=
            `<tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.age}</td>
               <td>${user.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}</td>
                <td>
                    <button class="btn btn-primary"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-user-id="${user.id}">
                        Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-user-id="${user.id}">                     
                        Delete</button>
                </td>
            </tr>`;
    }
    usersTable.innerHTML = usersTableHTML;
}


async function fillTableAboutCurrentUser(){
    const currentUserTable = document.getElementById("currentUserTable");
    const currentUser = await dataAboutCurrentUser();

    let currentUserTableHTML = "";
    currentUserTableHTML +=
        `<tr>
            <td>${currentUser.id}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.email}</td>
            <td>${currentUser.age}</td>
            <td>${currentUser.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}</td>
        </tr>`
    currentUserTable.innerHTML = currentUserTableHTML;
}