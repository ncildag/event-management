package com.tpe.service;

import com.tpe.domain.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.base-url}")
    private String appBaseUrl;

    public void sendRegistrationConfirmation(Registration registration) {

        String registrationUrl =
                appBaseUrl
                        + "/my-registration/"
                        + registration.getRegistrationCode();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(registration.getEmail());

        message.setSubject(
                "Kayıt Onayı / Anmeldebestätigung / Registration Confirmation - "
                        + registration.getEvent().getEventName()
        );

        message.setText(

                // =========================
                // TÜRKÇE
                // =========================

                "TÜRKÇE\n" +
                        "----------------------------------------\n\n" +

                        "Merhaba " + registration.getName() + ",\n\n" +

                        "Etkinlik kaydınız başarıyla tamamlanmıştır.\n\n" +

                        "Etkinlik: " + registration.getEvent().getEventName() + "\n" +
                        "Tarih: " + registration.getEvent().getDate() + "\n" +
                        "Saat: " + registration.getEvent().getTime() + "\n" +
                        "Adres: " + registration.getEvent().getAddress() + "\n\n" +

                        "Toplam kayıtlı kişi sayısı: " + registration.getTotalRegistration() + "\n" +
                        "Vegan menü sayısı: " + registration.getVeganMenuCount() + "\n" +
                        "Vegan menü alacak kişiler: " +
                        formatVeganNames(registration) + "\n" +
                        "Kayıt kodunuz: " + registration.getRegistrationCode() + "\n\n" +

                        "Kaydınızı görüntülemek, düzenlemek veya iptal etmek için:\n" +
                        registrationUrl + "\n\n" +

                        "Kaydınız için teşekkür ederiz.\n\n" +

                        "Saygılarımızla,\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // DEUTSCH
                        // =========================

                        "DEUTSCH\n" +
                        "----------------------------------------\n\n" +

                        "Hallo " + registration.getName() + ",\n\n" +

                        "Ihre Anmeldung für die Veranstaltung wurde erfolgreich bestätigt.\n\n" +

                        "Veranstaltung: " + registration.getEvent().getEventName() + "\n" +
                        "Datum: " + registration.getEvent().getDate() + "\n" +
                        "Uhrzeit: " + registration.getEvent().getTime() + "\n" +
                        "Adresse: " + registration.getEvent().getAddress() + "\n\n" +

                        "Anzahl der angemeldeten Personen: " + registration.getTotalRegistration() + "\n" +
                        "Anzahl der veganen Menüs: " + registration.getVeganMenuCount() + "\n" +
                        "Personen mit veganem Menü: " +
                        formatVeganNames(registration) + "\n" +
                        "Ihr Anmeldecode: " + registration.getRegistrationCode() + "\n\n" +

                        "Um Ihre Anmeldung anzusehen, zu bearbeiten oder zu stornieren:\n" +
                        registrationUrl + "\n\n" +

                        "Vielen Dank für Ihre Anmeldung.\n\n" +

                        "Freundliche Grüsse\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // ENGLISH
                        // =========================

                        "ENGLISH\n" +
                        "----------------------------------------\n\n" +

                        "Hello " + registration.getName() + ",\n\n" +

                        "Your registration for the event has been successfully confirmed.\n\n" +

                        "Event: " + registration.getEvent().getEventName() + "\n" +
                        "Date: " + registration.getEvent().getDate() + "\n" +
                        "Time: " + registration.getEvent().getTime() + "\n" +
                        "Address: " + registration.getEvent().getAddress() + "\n\n" +

                        "Total registered persons: " + registration.getTotalRegistration() + "\n" +
                        "Number of Vegan Menus: " + registration.getVeganMenuCount() + "\n" +
                        "Attendees with Vegan Menu: " +
                        formatVeganNames(registration) + "\n" +
                        "Registration Code: " + registration.getRegistrationCode() + "\n\n" +

                        "To view, edit or cancel your registration:\n" +
                        registrationUrl + "\n\n" +

                        "Thank you for your registration.\n\n" +

                        "Best regards,\n" +
                        "Event Management"
        );

        mailSender.send(message);
    }

    public void sendRegistrationUpdate(Registration registration) {

        String registrationUrl =
                appBaseUrl
                        + "/my-registration/"
                        + registration.getRegistrationCode();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(registration.getEmail());

        message.setSubject(
                "Kayıt Güncellendi / Anmeldung aktualisiert / Registration Updated - "
                        + registration.getEvent().getEventName()
        );

        message.setText(

                // =========================
                // TÜRKÇE
                // =========================

                "TÜRKÇE\n" +
                        "----------------------------------------\n\n" +

                        "Merhaba " + registration.getName() + ",\n\n" +

                        "Etkinlik kaydınız başarıyla güncellenmiştir.\n\n" +

                        "Etkinlik: " + registration.getEvent().getEventName() + "\n" +
                        "Tarih: " + registration.getEvent().getDate() + "\n" +
                        "Saat: " + registration.getEvent().getTime() + "\n" +
                        "Adres: " + registration.getEvent().getAddress() + "\n\n" +

                        "Toplam kayıtlı kişi sayısı: " + registration.getTotalRegistration() + "\n" +
                        "Vegan menü sayısı: " + registration.getVeganMenuCount() + "\n" +
                        "Vegan menü alacak kişiler: " +
                        formatVeganNames(registration) + "\n" +
                        "Kayıt kodunuz: " + registration.getRegistrationCode() + "\n\n" +

                        "Kaydınızı görüntülemek, düzenlemek veya iptal etmek için:\n" +
                        registrationUrl + "\n\n" +

                        "Saygılarımızla,\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // DEUTSCH
                        // =========================

                        "DEUTSCH\n" +
                        "----------------------------------------\n\n" +

                        "Hallo " + registration.getName() + ",\n\n" +

                        "Ihre Anmeldung für die Veranstaltung wurde erfolgreich aktualisiert.\n\n" +

                        "Veranstaltung: " + registration.getEvent().getEventName() + "\n" +
                        "Datum: " + registration.getEvent().getDate() + "\n" +
                        "Uhrzeit: " + registration.getEvent().getTime() + "\n" +
                        "Adresse: " + registration.getEvent().getAddress() + "\n\n" +

                        "Anzahl der angemeldeten Personen: " + registration.getTotalRegistration() + "\n" +
                        "Anzahl der veganen Menüs: " + registration.getVeganMenuCount() + "\n" +
                        "Personen mit veganem Menü: " +
                        formatVeganNames(registration) + "\n" +
                        "Ihr Anmeldecode: " + registration.getRegistrationCode() + "\n\n" +

                        "Um Ihre Anmeldung anzusehen, zu bearbeiten oder zu stornieren:\n" +
                        registrationUrl + "\n\n" +

                        "Freundliche Grüsse\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // ENGLISH
                        // =========================

                        "ENGLISH\n" +
                        "----------------------------------------\n\n" +

                        "Hello " + registration.getName() + ",\n\n" +

                        "Your registration for the event has been successfully updated.\n\n" +

                        "Event: " + registration.getEvent().getEventName() + "\n" +
                        "Date: " + registration.getEvent().getDate() + "\n" +
                        "Time: " + registration.getEvent().getTime() + "\n" +
                        "Address: " + registration.getEvent().getAddress() + "\n\n" +

                        "Total registered persons: " + registration.getTotalRegistration() + "\n" +
                        "Number of Vegan Menus: " + registration.getVeganMenuCount() + "\n" +
                        "Attendees with Vegan Menu: " +
                        formatVeganNames(registration) + "\n" +
                        "Registration Code: " + registration.getRegistrationCode() + "\n\n" +

                        "To view, edit or cancel your registration:\n" +
                        registrationUrl + "\n\n" +

                        "Best regards,\n" +
                        "Event Management"
        );

        mailSender.send(message);
    }

    public void sendRegistrationCancellation(Registration registration) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(registration.getEmail());

        message.setSubject(
                "Kayıt İptal Edildi / Anmeldung storniert / Registration Cancelled - "
                        + registration.getEvent().getEventName()
        );

        message.setText(

                // =========================
                // TÜRKÇE
                // =========================

                "TÜRKÇE\n" +
                        "----------------------------------------\n\n" +

                        "Merhaba " + registration.getName() + ",\n\n" +

                        "Etkinlik kaydınız başarıyla iptal edilmiştir.\n\n" +

                        "Etkinlik: " + registration.getEvent().getEventName() + "\n" +
                        "Tarih: " + registration.getEvent().getDate() + "\n" +
                        "Saat: " + registration.getEvent().getTime() + "\n" +
                        "Adres: " + registration.getEvent().getAddress() + "\n\n" +

                        "İptal edilen kayıt kodu: "
                        + registration.getRegistrationCode() + "\n\n" +

                        "Bu kayıt artık aktif değildir.\n" +
                        "Etkinliğe tekrar katılmak isterseniz yeni bir kayıt oluşturabilirsiniz.\n\n" +

                        "Saygılarımızla,\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // DEUTSCH
                        // =========================

                        "DEUTSCH\n" +
                        "----------------------------------------\n\n" +

                        "Hallo " + registration.getName() + ",\n\n" +

                        "Ihre Anmeldung für die Veranstaltung wurde erfolgreich storniert.\n\n" +

                        "Veranstaltung: " + registration.getEvent().getEventName() + "\n" +
                        "Datum: " + registration.getEvent().getDate() + "\n" +
                        "Uhrzeit: " + registration.getEvent().getTime() + "\n" +
                        "Adresse: " + registration.getEvent().getAddress() + "\n\n" +

                        "Stornierter Anmeldecode: "
                        + registration.getRegistrationCode() + "\n\n" +

                        "Diese Anmeldung ist nicht mehr aktiv.\n" +
                        "Wenn Sie wieder teilnehmen möchten, können Sie sich erneut anmelden.\n\n" +

                        "Freundliche Grüsse\n" +
                        "Event Management\n\n\n" +


                        // =========================
                        // ENGLISH
                        // =========================

                        "ENGLISH\n" +
                        "----------------------------------------\n\n" +

                        "Hello " + registration.getName() + ",\n\n" +

                        "Your registration for the event has been successfully cancelled.\n\n" +

                        "Event: " + registration.getEvent().getEventName() + "\n" +
                        "Date: " + registration.getEvent().getDate() + "\n" +
                        "Time: " + registration.getEvent().getTime() + "\n" +
                        "Address: " + registration.getEvent().getAddress() + "\n\n" +

                        "Cancelled Registration Code: "
                        + registration.getRegistrationCode() + "\n\n" +

                        "This registration is no longer active.\n" +
                        "If you would like to attend again, you can create a new registration.\n\n" +

                        "Best regards,\n" +
                        "Event Management"
        );

        mailSender.send(message);
    }

    private String formatVeganNames(Registration registration) {

        if (registration.getVeganMenuCount() == null
                || registration.getVeganMenuCount() == 0
                || registration.getVeganAttendeeNames() == null
                || registration.getVeganAttendeeNames().isBlank()) {

            return "-";
        }

        return registration.getVeganAttendeeNames();
    }

}