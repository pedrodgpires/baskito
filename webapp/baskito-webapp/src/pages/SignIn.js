import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const SignIn = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const response = await fetch('http://localhost:8080/user/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, password }),
            });

            if (!response.ok) {
                throw new Error('Sign in failed');
            }

            console.log('Signed in successfully');
            // Handle successful sign-in (e.g., redirect to login page, show success message, etc.)
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 dark:bg-gray-900">
            <form onSubmit={handleSubmit} className="p-8 bg-white dark:bg-gray-800 shadow-md rounded w-96">
                <h2 className="text-2xl font-bold text-gray-800 dark:text-gray-100 mb-6">Sign In</h2>
                {error && <p className="text-red-500 mb-4">{error}</p>}
                <label className="block text-gray-700 dark:text-gray-300 mb-2">Name:</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full px-4 py-2 border rounded focus:outline-none focus:ring"
                />
                <label className="block text-gray-700 dark:text-gray-300 mt-4 mb-2">Email:</label>
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
                    className="w-full bg-green-500 text-white py-2 mt-6 rounded hover:bg-green-600"
                >
                    Sign In
                </button>
                <p className="mt-4 text-gray-600 dark:text-gray-400 text-sm">
                    Already have an account? <Link to="/login" className="text-blue-500">Login</Link>
                </p>
            </form>
        </div>
    );
};

export default SignIn;