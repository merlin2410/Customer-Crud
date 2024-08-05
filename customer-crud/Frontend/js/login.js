//Getting elements of different components in html page

const mainContainer = document.getElementById('main-container');
const userNameField = document.getElementById('username');
const passwordField = document.getElementById('password');
const loginButton = document.getElementById('login-btn');

//This function is expected to run as soon as index.html hits. This will redirect to customer list page if token is stil valid
checkTokenAndRedirect();

//If login button is clicked, values from username and password fields are taken and sent to get token
loginButton.addEventListener('click',(event)=>{
    event.preventDefault();
    let userName = userNameField.value;
    let password = passwordField.value; 
    console.log(userName,password);
    getToken(userName,password);
})

//Function to get token. Takes username and password as arguements
async function getToken(userName,password){
    try {
        //Fetching the backend url with post method which respond with a token
        const response = await fetch('http://localhost:8080/admin/generateToken', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userName: userName,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        //Token is exracted from the response
        const token = await response.text();
        
        console.log('Token:', token); 

        //Token is store into local storage of browser for other apis to be fetched in future.
        //This avoids repeated login
        localStorage.setItem('authToken', token);

        //After login, redirects to Customer list window
        window.location.href = 'customerList.html';
    } catch (error) {
        console.error('Error:', error);
    }
}

//Checks the local storage for token. If valid, redirects to Customer list page
async function checkTokenAndRedirect(){
    try{
        //Exracts token from local storage
        const token = localStorage.getItem('authToken');
        
        //Checking whether token is still valid by hitting an api
        if(token){
           const response = await fetch('http://localhost:8080/customer/all',
            {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
           );
           //If response is there, redirects to Customer list page
           if(response.ok){
            window.location.href = 'customerList.html';
           }
        }
    }catch(error){
        console.error('Error:',error);
    }
}

