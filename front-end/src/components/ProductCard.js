import React, { useState } from 'react';

export default function ProductCard({ product, onAdd }) {
  const [adding, setAdding] = useState(false);

  function handleAdd() {
    if (adding) return;
    setAdding(true);
    try {
      onAdd && onAdd(product);
    } finally {
      // keep disabled state briefly for UX
      setTimeout(() => setAdding(false), 700);
    }
  }

  return (
    <div className="pc-card">
      <div className="pc-image-placeholder">No Image</div>
      <div className="pc-body">
        <h3 className="pc-title">{product.name}</h3>
        <p className="pc-desc">{product.description}</p>
        <div className="pc-row">
          <div className="pc-price">${product.price.toFixed(2)}</div>
          <button className="pc-add" onClick={handleAdd} disabled={adding}>
            {adding ? 'Added' : 'Add to cart'}
          </button>
        </div>
      </div>
    </div>
  );
}
