import React from 'react';
import './HomePage.css';
import ProductCard from './ProductCard';

const sampleProducts = [
  { id: 1, name: 'Classic T-Shirt', price: 19.99, description: 'Comfortable cotton t-shirt' },
  { id: 2, name: 'Sneakers', price: 59.99, description: 'Lightweight running shoes' },
  { id: 3, name: 'Coffee Mug', price: 9.5, description: 'Ceramic mug, 12oz' },
  { id: 4, name: 'Wireless Headphones', price: 89.0, description: 'Noise cancelling' },
  { id: 5, name: 'Backpack', price: 45.0, description: 'Water-resistant backpack' },
  { id: 6, name: 'Baseball Cap', price: 14.0, description: 'Adjustable fit' },
];

export default function HomePage() {
  return (
    <div className="hp-root">
      <nav className="hp-nav">
        <div className="hp-brand">ShopEasy</div>
        <div className="hp-actions">
          <button className="hp-cart">Cart (0)</button>
        </div>
      </nav>

      <header className="hp-hero">
        <h1>Welcome to ShopEasy</h1>
        <p>Quality products at great prices. Browse our featured collection below.</p>
      </header>

      <main className="hp-main">
        <h2>Featured Products</h2>
        <div className="hp-grid">
          {sampleProducts.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </div>
      </main>

      <footer className="hp-footer">© {new Date().getFullYear()} ShopEasy — Built for demo</footer>
    </div>
  );
}
