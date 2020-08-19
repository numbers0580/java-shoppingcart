package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.Cart;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.repositories.UserRepository;
import com.lambdaschool.shoppingcart.services.CartService;
import com.lambdaschool.shoppingcart.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HelperFunctions helperFunctions;

    @GetMapping(value = "/user/{userid}", produces = {"application/json"})
    public ResponseEntity<?> listAllCarts(@PathVariable long userid)
    {
        List<Cart> myCarts = cartService.findAllByUserId(userid);
        return new ResponseEntity<>(myCarts, HttpStatus.OK);
    }

    // Dropped the {userid} Path as advised by instructor
    @GetMapping(value = "/user", produces = {"application/json"})
    public ResponseEntity<?> authUser()
    {
        User theUser = userRepository.findByUsername(helperFunctions.getCurrentAuditor());
        List<Cart> currUser = cartService.findAllByUserId(theUser.getUserid());
        return new ResponseEntity<>(currUser, HttpStatus.OK);
    }

    @PostMapping(value = "/create/product", produces="application/json")
    public ResponseEntity<?> createNewCart() {
        //User myUser = userRepository.findByUsername(helperFunctions.getCurrentAuditor());

        cartService.createCart();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/cart/{cartId}",
            produces = {"application/json"})
    public ResponseEntity<?> getCartById(@PathVariable Long cartId)
    {
        Cart p = cartService.findCartById(cartId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping(value = "/create/user/{userid}/product/{productid}")
    public ResponseEntity<?> addNewCart(@PathVariable long userid, @PathVariable long productid)
    {
        User dataUser = new User();
        dataUser.setUserid(userid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(dataUser, dataProduct);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> updateCart(@PathVariable long cartid, @PathVariable long productid)
    {
        Cart dataCart = new Cart();
        dataCart.setUser(cartService.findCartById(cartid).getUser());
        dataCart.setCartid(cartid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(dataCart, dataProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> deleteFromCart(@PathVariable long cartid, @PathVariable long productid)
    {
        Cart dataCart = new Cart();
        dataCart.setCartid(cartid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.delete(dataCart, dataProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
