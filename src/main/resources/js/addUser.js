async function getRoles() {
    const response = await fetch("/api/users/roles");
    return await response.json();
}


async function createNewUser(user) {
    await fetch("/api/users",
        {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(user)})

}

async function addNewUserForm() {
    const newUserForm = document.getElementById("newUser");

    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = newUserForm.querySelector("#username").value.trim();
        const password = newUserForm.querySelector("#password").value.trim();
        const email = newUserForm.querySelector("#email").value.trim();
        const age = newUserForm.querySelector("#age").value.trim();

        const rolesSelected = document.getElementById("roles");



        let allRole = await getRoles();
        let AllRoles = {};
        for (let role of allRole) {
            AllRoles[role.name] = role.id;
        }
        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            if (Object.keys(AllRoles).indexOf(option.value) != -1) {
                roles.push({roleId: AllRoles[option.value], name: option.value});
            }
        }


        const newUserData = {
            username: name,
            password: password,
            email: email,
            age: age,
            roles: roles
        };

        await createNewUser(newUserData);
        newUserForm.reset();

        document.querySelector('a#show-users-table').click();
        await fillTableOfAllUsers();
    });
}