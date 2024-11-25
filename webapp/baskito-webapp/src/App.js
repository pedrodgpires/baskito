import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignIn from './pages/SignIn';
import Dashboard from './pages/Dashboard';

const App = () => {
    const [darkMode, setDarkMode] = useState(false);

    useEffect(() => {
        document.documentElement.classList.toggle('dark', darkMode);
    }, [darkMode]);

    return (
        <Router>
            <div className="p-4">
                <button
                    onClick={() => setDarkMode(!darkMode)}
                    className="mb-4 px-4 py-2 bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-100 rounded"
                >
                    Toggle {darkMode ? 'Light' : 'Dark'} Mode
                </button>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/sign-in" element={<SignIn />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
