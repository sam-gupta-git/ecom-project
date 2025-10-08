import React, { useState } from 'react';
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
  const [cart, setCart] = useState([]);
  const [toast, setToast] = useState(null);

  function addToCart(product) {
    setCart((c) => [...c, product]);
    setToast(`${product.name} added to cart`);
    // clear toast after 1.6s
    setTimeout(() => setToast(null), 1600);
  }

  function clearCart() {
    setCart([]);
  }

  return (
    <div className="hp-root">
      <nav className="hp-nav">
        <div className="hp-brand">ShopEasy</div>
        <div className="hp-actions">
          <button className="hp-cart">Cart ({cart.length})</button>
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
            <ProductCard key={p.id} product={p} onAdd={() => addToCart(p)} />
          ))}
        </div>

        {cart.length > 0 && (
          <div className="hp-cart-preview">
            <strong>Cart ({cart.length})</strong>
            <ul>
+              {cart.map((it, idx) => (
                <li key={idx}>{it.name} — ${it.price.toFixed(2)}</li>
              ))}
            </ul>
            <div style={{ marginTop: 8 }}>
              <button onClick={clearCart} className="pc-add">Clear cart</button>
            </div>
          </div>
        )}

        {toast && <div className="hp-toast">{toast}</div>}
      </main>

      <footer className="hp-footer">© {new Date().getFullYear()} ShopEasy — Built for demo</footer>
    </div>
  );
}
