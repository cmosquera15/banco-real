import './App.css';
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { SignUp } from './components/SignUp';
import { Login } from './components/Login';
import { Home } from './components/Home';
import { BankPolicies } from './components/BankPolicies';
import { EthicsCode } from './components/EthicsCode';
import logo_header from './components/images/banco-real-logo-1.png';
import logo_footer from './components/images/banco-real-logo-2.png';

function App() {
  const [showBankPolitics, setShowBankPolitics] = useState(false);
  const [showEthicsCode, setShowEthicsCode] = useState(false);
  const [userManagementComponent, setUserManagementComponent] = useState(null);
  const [currentComponent, setCurrentComponent] = useState(null);
  const [user, setUser] = useState(null);

  const handleTitleClick = () => {
    setUserManagementComponent(null);
    document.getElementById('App-SignUp-btn').style.display = 'block';
    document.getElementById('App-Login-btn').style.display = 'block';
  };

  const handleBankPoliticsClick = () => {
    setShowBankPolitics(true);
  };

  const handleEthicsCodeClick = () => {
    setShowEthicsCode(true);
  };

  const handleSignUpClick = () => {
    setUserManagementComponent(<SignUp onSignUpSuccess={handleLoginSuccess} />);
    document.getElementById('App-SignUp-btn').style.display = 'none';
    document.getElementById('App-Login-btn').style.display = 'none';
  };

  const handleLoginClick = () => {
    setUserManagementComponent(<Login onLoginSuccess={handleLoginSuccess} />);
    document.getElementById('App-SignUp-btn').style.display = 'none';
    document.getElementById('App-Login-btn').style.display = 'none';
  };

  const handleLoginSuccess = (userData) => {
    setUser(userData);
    setCurrentComponent(<Home user={userData} onLogout={handleLogout} />);
    setUserManagementComponent(null);
    document.getElementById('App-title').style.cursor = 'default';
    document.getElementById('App-main-user-management').style.display = 'none';
    document.getElementById('App-SignUp-btn').style.display = 'none';
    document.getElementById('App-Login-btn').style.display = 'none';
    document.getElementById('App-footer').style.display = 'none';
    document.getElementById('App-main').classList.add('App-main-expanded');
  };

  const handleLogout = () => {
    setUser(null);
    setCurrentComponent(null);
    document.getElementById('App-title').style.cursor = 'pointer';
    document.getElementById('App-main-user-management').style.display = 'flex';
    document.getElementById('App-SignUp-btn').style.display = 'block';
    document.getElementById('App-Login-btn').style.display = 'block';
    document.getElementById('App-footer').style.display = 'flex';
    document.getElementById('App-main').classList.remove('App-main-expanded');
  };

  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <img src={logo_header} alt="Banco-Real-logo" id='App-title' onClick={handleTitleClick} />
          <div id='App-header-politic-and-ethic'>
            <button onClick={handleBankPoliticsClick}>Políticas del banco</button>
            <button onClick={handleEthicsCodeClick}>Código de ética</button>
          </div>
        </header>

        <main className='App-main' id='App-main'>
          <div id='App-main-user-management' className='App-main-user-management'>
            <button id='App-Login-btn' onClick={handleLoginClick}>Iniciar sesión</button>
            <button id='App-SignUp-btn' onClick={handleSignUpClick}>Registrarse</button>
            {userManagementComponent}
          </div>
          <Routes>
            <Route path="/" element={<Home user={user} onLogout={handleLogout} />} />
          </Routes>
          {currentComponent}
        </main>

        <footer className='App-footer' id='App-footer'>
          <p>Banco Real S.A. todos los derechos reservados 2024</p>
          <img src={logo_footer} alt="Banco-Real-logo" />
        </footer>

        {showBankPolitics && (
          <BankPolicies onClose={() => setShowBankPolitics(false)} />
        )}

        {showEthicsCode && (
          <EthicsCode onClose={() => setShowEthicsCode(false)} />
        )}
      </div>
    </Router>
  );
}

export default App;
