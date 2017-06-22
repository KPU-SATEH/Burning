using System;
using System.Windows;
using System.ComponentModel;
using System.Windows.Media.Imaging;

namespace ContentProvider
{
    /// <summary>
    /// Interaction logic for imageView.xaml
    /// </summary>
    public partial class imageView : Window
    {
        int num = 0;
        int maxNum = Data.Count();
        BitmapImage img;

        void cleanBackground(object sender, CancelEventArgs e)
        {
            MainWindow mw = ((MainWindow)(Application.Current.MainWindow));

            mw.backImage.ImageSource = null;
        }
        
        public imageView()
        {
            InitializeComponent();

            img = new BitmapImage(new Uri(Data.Read(num)));

            image.Source = img;
        }

        private void buttonLeft_Click(object sender, RoutedEventArgs e)
        {
            if (num == 0)
                return;
            else
            {
                num--;

                img = new BitmapImage(new Uri(Data.Read(num)));
                image.Source = img;
            }
        }

        private void buttonRight_Click(object sender, RoutedEventArgs e)
        {
            if (num == maxNum - 1)
                return;
            else
            {
                num++;

                img = new BitmapImage(new Uri(Data.Read(num)));
                image.Source = img;
            }
        }

        private void ImageButton_Click(object sender, RoutedEventArgs e)
        {
            MainWindow mw = ((MainWindow)(Application.Current.MainWindow));

            try
            {
                BitmapImage bi = new BitmapImage(new Uri(Data.Read(num)));

                mw.backImage.ImageSource = bi;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }
        }
    }
}
