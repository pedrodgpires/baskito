import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);


    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const response = await fetch('http://localhost:8080/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json();
            console.log('Logged in with:', data);
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 dark:bg-gray-900">
            <form onSubmit={handleSubmit} className="p-8 bg-white dark:bg-gray-800 shadow-md rounded w-96">
                <h2 className="text-2xl font-bold text-gray-800 dark:text-gray-100 mb-6 text-center">Login</h2>
                {error && <p className="text-red-500 mb-4">{error}</p>}
                <label className="block text-gray-700 dark:text-gray-300 mb-2">E-mail:</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-4 py-2 border rounded focus:outline-none focus:ring"
                />
                <label className="block text-gray-700 dark:text-gray-300 mt-4 mb-2">Password:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full px-4 py-2 border rounded focus:outline-none focus:ring"
                />
                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 mt-6 rounded hover:bg-blue-600"
                >
                    Login
                </button>
                <p className="mt-4 text-gray-600 dark:text-gray-400 text-sm text-center">
                    Don't have an account? <Link to="/sign-in" className="text-blue-500">Sign In</Link>
                </p>
            </form>
        </div>
    );
};

export default Login;
