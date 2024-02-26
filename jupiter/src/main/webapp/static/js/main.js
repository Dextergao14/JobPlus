/**
 * 
 */



init();

// entry 
    function init(){
		document.querySelector('#login-form-btn').addEventListener('click', onSessionInvalid);
		document.querySelector('#register-form-btn').addEventListener('click', showRegisterForm);
		document.querySelector('#login-btn').addEventListener('click', login);
		
		document.querySelector('#register-btn').addEventListener('click',
				register);
				
		document.addEventListener('DOMContentLoaded', function() {
    		document.getElementById('search-btn').addEventListener('click', searchJobs);
		});

		document.querySelector('#nearby-btn').addEventListener('click', loadNearbyItems);

		document.querySelector('#fav-btn').addEventListener('click', loadFavoriteItems);
	    document.querySelector('#recommend-btn').addEventListener('click', loadRecommendedItems);
	    
		
		
		validateSession();
	}
	
function searchJobs() {
	
	showLoadingIndicator();
	
    var keyword = document.getElementById('keyword').value;
    var location = document.getElementById('location').value;

    // 构建请求URL
    var url = './search?app_id=bfa0513a&app_key=092433561360b79effdca321ed0eaf68&results_per_page=50&what=' +
        encodeURIComponent(keyword) + '&where=' + encodeURIComponent(location) + '&distance=100&max_days_old=60&sort_by=salary&full_time=1';

    fetch(url)
        .then(response => response.json())
        .then(data => {
			hideLoadingIndicator();
			
            console.log('Search results:', data);
            displaySearchResults(data); // 显示搜索结果
        })
        .catch(error => console.error("Error fetching jobs:", error));
}
	
function displaySearchResults(results) {
    var itemList = document.getElementById('item-list');
    itemList.innerHTML = ''; // 清除之前的结果

    // 遍历搜索结果
    results.forEach(function(item) {
        // 创建新的<li>元素来展示每个搜索结果
        var li = document.createElement('li');
        li.classList.add('item');

        // 初始化keywords字符串
        var keywordsString = item.keywords.join(', ');

        // 设置元素的HTML内容，突出显示keywords
        var itemHTML = `
            <div>
                <h3><a href="${item.url}" target="_blank">${item.name}</a></h3>
                <p><strong>Keywords:</strong> ${keywordsString}</p>
            </div>
        `;

        li.innerHTML = itemHTML;
        itemList.appendChild(li);
    });
}

	
	//check session
	function validateSession(){
		onSessionInvalid();
		// The request parameters
	    var url = './login';
	    var req = JSON.stringify({});

	    // display loading message
	    showLoadingMessage('Validating session...');

	    // make AJAX call
	    ajax('GET', url, req,
	      // session is still valid
	      function(res) {
	        var result = JSON.parse(res);

	        if (result.status === 'OK') {
	        	// case2: session valid
	        	   onSessionValid(result);
	        }
	      }, function(){
	    	 // case1: session invalid
	    	  console.log('session is invalid');
	      });
	}
	
	// hide register form, logout button, dummy data, etc
	// show login form
	function onSessionInvalid(){
	    var loginForm = document.querySelector('#login-form');
	    var registerForm = document.querySelector('#register-form');
	    var itemNav = document.querySelector('#item-nav');
	    var itemList = document.querySelector('#item-list');
	    var avatar = document.querySelector('#avatar');
	    var welcomeMsg = document.querySelector('#welcome-msg');
	    var logoutBtn = document.querySelector('#logout-link');
	    
	    hideElement(itemNav);
	    hideElement(itemList);
	    hideElement(avatar);
	    hideElement(logoutBtn);
	    hideElement(welcomeMsg);
	    hideElement(registerForm);

	    clearLoginError();
	    showElement(loginForm);
	}
	
	function onSessionValid(result) {
	    user_id = result.user_id;
	    user_fullname = result.name;

	    var loginForm = document.querySelector('#login-form');
	    var registerForm = document.querySelector('#register-form');
	    var itemNav = document.querySelector('#item-nav');
	    var itemList = document.querySelector('#item-list');
	    var avatar = document.querySelector('#avatar');
	    var welcomeMsg = document.querySelector('#welcome-msg');
	    var logoutBtn = document.querySelector('#logout-link');

	    welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

	    showElement(itemNav);
	    showElement(itemList);
	    showElement(avatar);
	    showElement(welcomeMsg);
	    showElement(logoutBtn, 'inline-block');
	    hideElement(loginForm);
	    hideElement(registerForm);
	    
	    initGeoLocation();
	}
	
	
	  
	
	function initGeoLocation() {
	    if (navigator.geolocation) {
	      navigator.geolocation.getCurrentPosition(
	        onPositionUpdated,
	        onLoadPositionFailed, {
	          maximumAge: 60000
	        });
	      showLoadingMessage('Retrieving your location...');
	    } else {
	      onLoadPositionFailed();
	    }
	  }
	
	function onPositionUpdated(position) {
	    lat = position.coords.latitude;
	    lng = position.coords.longitude;
	    
        loadNearbyItems();
	}

	function onLoadPositionFailed() {
	    console.warn('navigator.geolocation is not available');
                 getLocationFromIP();
	}
	
             function getLocationFromIP() {
		// get location from http://ipinfo.io/json
		var url = 'http://ipinfo.io/json'
		var data = null;

		ajax('GET', url, data, function(res) {
			var result = JSON.parse(res);
			if ('loc' in result) {
				var loc = result.loc.split(',');
				lat = loc[0];
				lng = loc[1];
				loadNearbyItems();
			} else {
				console.warn('Getting location by IP failed.');
			}
		}, function(){
			console.log('error in fetching location')
		});
	}
	
	function login() {
	    var username = document.querySelector('#username').value;
	    var password = document.querySelector('#password').value;
	    password = md5(username + md5(password));

	    // The request parameters
	    var url = './login';
	    var req = JSON.stringify({
	      user_id : username,
	      password : password,
	    });

	    ajax('POST', url, req,
	      // successful callback
	      function(res) {
	        var result = JSON.parse(res);

	        // successfully logged in
	        if (result.status === 'OK') {
	        	   onSessionValid(result);
	        }
	      },
	      // error
	      function() {
	        showLoginError();
	      });
	  }
	
	// show register form
	function showRegisterForm() {
	        var loginForm = document.querySelector('#login-form');
	        var registerForm = document.querySelector('#register-form');
	        var itemNav = document.querySelector('#item-nav');
	        var itemList = document.querySelector('#item-list');
	        var avatar = document.querySelector('#avatar');
	        var welcomeMsg = document.querySelector('#welcome-msg');
	        var logoutBtn = document.querySelector('#logout-link');

	        hideElement(itemNav);
	        hideElement(itemList);
	        hideElement(avatar);
	        hideElement(logoutBtn);
	        hideElement(welcomeMsg);
	        hideElement(loginForm);
	    
	        clearRegisterResult();
	        showElement(registerForm);
	  }  

	  function register() {
		var username = document.querySelector('#register-username').value;
		var password = document.querySelector('#register-password').value;
		var firstName = document.querySelector('#register-first-name').value;
		var lastName = document.querySelector('#register-last-name').value;

		if (username === "" || password == "" || firstName === ""
				|| lastName === "") {
			showRegisterResult('Please fill in all fields');
			return
		}

		if (username.match(/^[a-z0-9_]+$/) === null) {
			showRegisterResult('Invalid username');
			return
		}
		
		password = md5(username + md5(password));

		// The request parameters
		var url = './register';
		var req = JSON.stringify({
			user_id : username,
			password : password,
			first_name : firstName,
			last_name : lastName,
		});

		ajax('POST', url, req,
		// successful callback
		function(res) {
			var result = JSON.parse(res);

			// successfully logged in
			if (result.status === 'OK') {
				showRegisterResult('Succesfully registered');
			} else {
				showRegisterResult('User already existed');
			}
		},

		// error
		function() {
			showRegisterResult('Failed to register');
		}, true);
	}

	function showRegisterResult(registerMessage) {
		document.querySelector('#register-result').innerHTML = registerMessage;
	}

	function clearRegisterResult() {
		document.querySelector('#register-result').innerHTML = '';
	}
	
	function showLoadingIndicator() {
    	document.getElementById('loading-indicator').style.display = 'block';
}

	function hideLoadingIndicator() {
    	document.getElementById('loading-indicator').style.display = 'none';
	}