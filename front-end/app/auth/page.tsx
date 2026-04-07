'use client';

import { useState } from 'react';
import { loginApi } from '@/services/authService';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage('Đang kiểm tra...');
    
    try {
      const result = await loginApi({ email, password });
      console.log('Backend trả về:', result);
      setMessage('Đăng nhập thành công! Check console nhé.');
      // Lưu token vào localStorage hoặc Cookie tại đây
    } catch (error: any) {
      setMessage('Lỗi: ' + error.message);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <form onSubmit={handleSubmit} className="p-8 border rounded shadow-md w-80">
        <h1 className="text-2xl font-bold mb-4">Test Login</h1>
        <input
          type="email"
          placeholder="Email"
          className="w-full p-2 mb-3 border rounded text-black"
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          className="w-full p-2 mb-3 border rounded text-black"
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded">
          Đăng nhập
        </button>
        <p className="mt-4 text-sm text-center font-semibold">{message}</p>
      </form>
    </div>
  );
}