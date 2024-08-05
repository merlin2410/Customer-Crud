const mainContainer = document.getElementById('main-container');
const firstNameField = document.getElementById('firstName');
const lastNameField = document.getElementById('lastName');
const addressField = document.getElementById('address');
const streetField = document.getElementById('street');
const cityField = document.getElementById('city');
const stateField = document.getElementById('state');
const emailField = document.getElementById('email');
const phoneField = document.getElementById('phone');
const submitButton = document.getElementById('submit-btn');


checkTokenAndRedirect();

submitButton.addEventListener('click',(event)=>{
    event.preventDefault();
    let firstName = firstNameField.value;
    let lastName = lastNameField.value;
    let address = addressField.value;
    let street = streetField.value;
    let city = cityField.value;
    let state = stateField.value;
    let email = emailField.value;
    let phone = phoneField.value;

    console.log(firstName,lastName);
    addCustomer(firstName,lastName,address,street,city,state,email,phone);
})


async function addCustomer(firstName,lastName,address,street,city,state,email,phone){
    const token = localStorage.getItem('authToken');
    try {
        const response = await fetch('http://localhost:8080/customer/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization':`Bearer ${token}`
            },
            body: JSON.stringify({
                first_name: firstName,
                last_name: lastName,
                address: address,
                street: street,
                city: city,
                state: state,
                email: email,
                phone: phone
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const message = await response.text();
        console.log('response',response);
        console.log('text:', message); 
        alert(message);
        window.location.href = 'customerList.html';
        
    } catch (error) {
        console.error('Error:', error);
    }
}

async function checkTokenAndRedirect(){
    try{
        const token = localStorage.getItem('authToken');
        if(token){
           const response = await fetch('http://localhost:8080/customer/all',
            {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
           );
           if(!response.ok){
            window.location.href = 'index.html';
           }
        }
    }catch(error){
        console.error('Error:',error);
    }
}

