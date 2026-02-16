document.addEventListener("DOMContentLoaded", function() {
    // Fetch products from the backend API
    fetch('http://localhost:8080/api/products')
        .then(response => response.json())
        .then(data => {
            const productList = document.getElementById('products');
            data.forEach(product => {
                const productItem = document.createElement('li');
                productItem.textContent = `${product.name} - ₺${product.price}`;
                productList.appendChild(productItem);
            });
        })
        .catch(error => console.error('Error fetching products:', error));
});
