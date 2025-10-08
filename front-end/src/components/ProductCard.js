import React from 'react';

export default function ProductCard({ product }) {
  return (
    <div className="pc-card">
      <div className="pc-image-placeholder">No Image</div>
      <div className="pc-body">
        <h3 className="pc-title">{product.name}</h3>
        <p className="pc-desc">{product.description}</p>
        <div className="pc-row">
          <div className="pc-price">${product.price.toFixed(2)}</div>
          <button className="pc-add">Add to cart</button>
        </div>
      </div>
    </div>
  );
}
