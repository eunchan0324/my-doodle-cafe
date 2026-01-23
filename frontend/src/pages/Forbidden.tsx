import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function Forbidden() {
  const navigate = useNavigate();

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#fef2f2',
        padding: '20px',
      }}
    >
      <div
        style={{
          fontSize: '80px',
          marginBottom: '16px',
        }}
      >
        🚫
      </div>
      <h1
        style={{
          fontSize: '24px',
          fontWeight: 'bold',
          color: '#dc2626',
          marginBottom: '8px',
        }}
      >
        접근 권한이 없습니다
      </h1>
      <p
        style={{
          color: '#6b7280',
          marginBottom: '24px',
          textAlign: 'center',
        }}
      >
        이 페이지에 접근할 권한이 없습니다.
        <br />
        관리자에게 문의하세요.
      </p>
      <div style={{ display: 'flex', gap: '12px' }}>
        <button
          onClick={() => navigate(-1)}
          style={{
            padding: '10px 20px',
            backgroundColor: '#e5e7eb',
            color: '#374151',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            fontSize: '14px',
          }}
        >
          이전 페이지
        </button>
        <button
          onClick={() => navigate('/')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#dc2626',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            fontSize: '14px',
          }}
        >
          홈으로
        </button>
      </div>
    </div>
  );
}
